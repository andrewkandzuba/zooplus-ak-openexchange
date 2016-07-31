package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.protocol.rest.v1.LoginResponse;
import com.zooplus.openexchange.protocol.rest.v1.RegistrationRequest;
import com.zooplus.openexchange.protocol.rest.v1.RegistrationResponse;
import com.zooplus.openexchange.starters.ControllersStarter;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

import static com.zooplus.openexchange.controllers.v1.CasProtocol.*;
import static com.zooplus.openexchange.security.filters.DataSourceAuthenticationFilter.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllersStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("controllers")
public class TestUserAndSessionControllers extends TestMockedClient {
    @Autowired
    private CsrfTokenRepository csrfTokenRepository;

    @Test
    public void testUserRegistration() throws Exception {
        // Request database
        final String userName = "ak";
        final String userPassword = "pwd";
        final String userEmail = "ak@zooplus.com";

        // Remember creation timestamp
        final long preUserCreationTimeStamp = System.currentTimeMillis();

        // Mock new user in not in db
        User user = new User(userName, passwordEncoder.encode(userPassword), userEmail, Collections.singleton(new Role(getNextId(), "USER")));
        user.setId(getNextId());
        user.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        user .setEnabled(true);
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);

        // Create request
        RegistrationRequest req = new RegistrationRequest();
        req.setName(userName);
        req.setPassword(userPassword);
        req.setEmail(userEmail);

        // Send request
        ResponseEntity<RegistrationResponse> resp =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + USER_RESOURCE,
                                HttpMethod.PUT,
                                RestClient.build(
                                        new Pair<>("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                                        new Pair<>(X_AUTH_TOKEN_HEADER, getAdminSessionToken())
                                ),
                                req,
                                RegistrationResponse.class);

        // Analyze response
        Assert.assertNotNull(resp);
        Assert.assertEquals(HttpStatus.OK, resp.getStatusCode());
        Assert.assertNotNull(resp.getBody().getId());

        // Mock new user in db
        long newUserId = resp.getBody().getId();
        Mockito.when(userRepository.findOne(newUserId)).thenReturn(user);
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(user);

        // Send the same request second time => HttpStatus.CONFLICT
        resp =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + USER_RESOURCE,
                                HttpMethod.PUT,
                                RestClient.build(
                                        new Pair<>("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                                        new Pair<>(X_AUTH_TOKEN_HEADER, getAdminSessionToken())
                                ),
                                req,
                                RegistrationResponse.class);

        // Analyze response
        Assert.assertNotNull(resp);
        Assert.assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());

        // Fetch user directly from repository by Id
        User existedUser = userRepository.findOne(newUserId);
        Assert.assertNotNull(existedUser);
        Assert.assertEquals(userName, existedUser.getName());
        Assert.assertTrue(passwordEncoder.matches(userPassword, existedUser.getPassword()));
        Assert.assertEquals(userEmail, existedUser.getEmail());
        Assert.assertNotNull(existedUser.getCreatedAt());
        Assert.assertTrue(preUserCreationTimeStamp < existedUser.getCreatedAt().getTime());
        Assert.assertTrue(existedUser.getEnabled());
        Assert.assertEquals(1, existedUser.getRoles().size());
        Assert.assertTrue(existedUser.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("USER")));

        // Mock db for login
        Mockito.when(userRepository.findByNameAndPassword(user.getName(), user.getPassword())).thenReturn(user);

        // Login with a regular user
        ResponseEntity<LoginResponse> loginResp =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + LOGIN_RESOURCE,
                                HttpMethod.POST,
                                RestClient.build(
                                        new Pair<>("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                                        new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                        new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())
                                ),
                                LoginResponse.class);

        // Analyze login response
        Assert.assertNotNull(loginResp);
        Assert.assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        Assert.assertTrue(loginResp.hasBody());
        String newUserToken = loginResp.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertNotNull(newUserToken);
        Assert.assertNotEquals(getAdminSessionToken(), loginResp);

        // Switch user session
        mockUserRedisSession(user, newUserToken);

        // Try to add new user with regular user role
        resp =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + USER_RESOURCE,
                                HttpMethod.PUT,
                                RestClient.build(
                                        new Pair<>("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE),
                                        new Pair<>(X_AUTH_TOKEN_HEADER, newUserToken)
                                ),
                                req,
                                RegistrationResponse.class);

        // Analyze response
        Assert.assertNotNull(resp);
        Assert.assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
    }
}

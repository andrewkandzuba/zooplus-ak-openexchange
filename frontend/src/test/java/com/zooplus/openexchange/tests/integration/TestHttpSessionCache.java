package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.protocol.v1.Logoutresponse;
import com.zooplus.openexchange.protocol.v1.Sessiondetailsresponse;
import com.zooplus.openexchange.starters.IntegrationStarter;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.Version.*;
import static com.zooplus.openexchange.security.filters.DataSourceAuthenticationFilter.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestHttpSessionCache extends TestLocalRestClient {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${local.server.port}")
    private int port;

    @Test
    public void testSessionCache() throws Exception {
        Role role = roleRepository.findByName("USER");
        Assert.assertNotNull(role);

        User user = new User("testUser", passwordEncoder.encode("testUserPassword"), "testUser@zooplus.com", Collections.singleton(role));
        user = userRepository.saveAndFlush(user);
        Assert.assertNotNull(user);
        Assert.assertTrue(userRepository.exists(user.getId()));

        // Login for the first time
        ResponseEntity<Loginresponse> loginResponse = getRestClient()
                .exchange(
                        USER_LOGIN_PATH,
                        HttpMethod.POST,
                        RestClient.build(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        Loginresponse.class);
        Assert.assertNotNull(loginResponse);
        Assert.assertNotNull(loginResponse.getHeaders());
        String firstSessionToken = loginResponse.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertFalse(firstSessionToken.equals(""));
        HttpHeaders firstSessionHeader = loginResponse.getHeaders();

        // Try to access user resource
        ResponseEntity<Sessiondetailsresponse> sessionDetailsResponse = getRestClient()
                .exchange(
                        USER_SESSION_DETAILS_PATH,
                        HttpMethod.GET,
                        firstSessionHeader,
                        Optional.empty(),
                        Sessiondetailsresponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(sessionDetailsResponse.hasBody());
        Assert.assertEquals(sessionDetailsResponse.getBody().getSessionId(), firstSessionToken);

        //  Login with a same user again => second session
        loginResponse = getRestClient()
                .exchange(
                        USER_LOGIN_PATH,
                        HttpMethod.POST,
                        RestClient.build(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        Loginresponse.class);
        Assert.assertNotNull(loginResponse);
        Assert.assertNotNull(loginResponse.getHeaders());
        String secondSessionToken = loginResponse.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertFalse(secondSessionToken.equals(""));
        HttpHeaders secondSessionHeader = loginResponse.getHeaders();

        // Test there are two different sessions
        Assert.assertNotEquals(firstSessionToken, secondSessionToken);

        // Try to access user resource on behalf on second session
        sessionDetailsResponse = getRestClient()
                .exchange(
                        USER_SESSION_DETAILS_PATH,
                        HttpMethod.GET,
                        secondSessionHeader,
                        Optional.empty(),
                        Sessiondetailsresponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(sessionDetailsResponse.hasBody());
        Assert.assertEquals(sessionDetailsResponse.getBody().getSessionId(), secondSessionToken);

        // Logout from the second session
        ResponseEntity<Logoutresponse> logoutResponse = getRestClient()
                .exchange(
                        USER_LOGOUT_PATH,
                        HttpMethod.POST,
                        RestClient.build(
                                new Pair<>(X_AUTH_TOKEN_HEADER, secondSessionToken)),
                        Optional.empty(),
                        Logoutresponse.class
                );
        Assert.assertNotNull(logoutResponse);
        Assert.assertNotNull(logoutResponse.getBody());
        Assert.assertTrue(logoutResponse.getBody().getSessionLifeTime() > 0);

        // Not able to access API with invalidated token
        sessionDetailsResponse = getRestClient()
                .exchange(
                        USER_SESSION_DETAILS_PATH,
                        HttpMethod.GET,
                        secondSessionHeader,
                        Optional.empty(),
                        Sessiondetailsresponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);

        // But still can do it with valid token
        sessionDetailsResponse = getRestClient()
                .exchange(
                        USER_SESSION_DETAILS_PATH,
                        HttpMethod.GET,
                        firstSessionHeader,
                        Optional.empty(),
                        Sessiondetailsresponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.OK);
    }
}

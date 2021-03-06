package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.database.domain.Role;
import com.zooplus.openexchange.database.domain.User;
import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import com.zooplus.openexchange.protocol.cas.LoginResponse;
import com.zooplus.openexchange.protocol.cas.LogoutResponse;
import com.zooplus.openexchange.protocol.cas.SessionDetailsResponse;
import com.zooplus.openexchange.starters.IntegrationStarter;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Optional;

import static com.zooplus.openexchange.protocol.cas.MetaInfo.LOGIN_RESOURCE;
import static com.zooplus.openexchange.protocol.cas.MetaInfo.LOGOUT_RESOURCE;
import static com.zooplus.openexchange.protocol.cas.MetaInfo.SESSION_RESOURCE;
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
        ResponseEntity<LoginResponse> loginResponse = getRestClient()
                .exchange(
                        LOGIN_RESOURCE,
                        HttpMethod.POST,
                        RestClient.build(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())
                        ),
                        LoginResponse.class);
        Assert.assertNotNull(loginResponse);
        Assert.assertNotNull(loginResponse.getHeaders());
        String firstSessionToken = loginResponse.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertFalse(firstSessionToken.equals(""));
        HttpHeaders firstSessionHeader = loginResponse.getHeaders();

        // Try to access user resource
        ResponseEntity<SessionDetailsResponse> sessionDetailsResponse = getRestClient()
                .exchange(
                        SESSION_RESOURCE,
                        HttpMethod.GET,
                        firstSessionHeader,
                        SessionDetailsResponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(HttpStatus.OK, sessionDetailsResponse.getStatusCode());
        Assert.assertNotNull(sessionDetailsResponse.hasBody());
        Assert.assertEquals(sessionDetailsResponse.getBody().getSessionId(), firstSessionToken);

        //  Login with a same user again => second session
        loginResponse = getRestClient()
                .exchange(
                        LOGIN_RESOURCE,
                        HttpMethod.POST,
                        RestClient.build(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        LoginResponse.class);
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
                        SESSION_RESOURCE,
                        HttpMethod.GET,
                        secondSessionHeader,
                        Optional.empty(),
                        SessionDetailsResponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(sessionDetailsResponse.hasBody());
        Assert.assertEquals(sessionDetailsResponse.getBody().getSessionId(), secondSessionToken);

        // Logout from the second session
        ResponseEntity<LogoutResponse> logoutResponse = getRestClient()
                .exchange(
                        LOGOUT_RESOURCE,
                        HttpMethod.POST,
                        secondSessionHeader,
                        LogoutResponse.class
                );
        Assert.assertNotNull(logoutResponse);
        Assert.assertNotNull(logoutResponse.getBody());
        Assert.assertTrue(logoutResponse.getBody().getSessionLifeTime() > 0);

        // Not able to access API with invalidated token
        sessionDetailsResponse = getRestClient()
                .exchange(
                        SESSION_RESOURCE,
                        HttpMethod.GET,
                        secondSessionHeader,
                        SessionDetailsResponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.UNAUTHORIZED);

        // But still can do it with valid token
        sessionDetailsResponse = getRestClient()
                .exchange(
                        SESSION_RESOURCE,
                        HttpMethod.GET,
                        firstSessionHeader,
                        SessionDetailsResponse.class
                );
        Assert.assertNotNull(sessionDetailsResponse);
        Assert.assertEquals(sessionDetailsResponse.getStatusCode(), HttpStatus.OK);
    }
}

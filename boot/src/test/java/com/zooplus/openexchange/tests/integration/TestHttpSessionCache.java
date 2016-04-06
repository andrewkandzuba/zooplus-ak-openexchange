package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.service.database.domain.Role;
import com.zooplus.openexchange.service.database.domain.User;
import com.zooplus.openexchange.service.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Optional;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_HELLO_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.*;


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
                        USER_AUTHENTICATE_PATH,
                        HttpMethod.POST,
                        RestClient.headersFrom(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        Loginresponse.class);
        Assert.assertNotNull(loginResponse);
        Assert.assertNotNull(loginResponse.getHeaders());
        String firstSessionToken = loginResponse.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertFalse(firstSessionToken.equals(""));

        // Try to access user resource
        ResponseEntity<String> helloResponse = getRestClient()
                .exchange(
                        USER_HELLO_PATH,
                        HttpMethod.GET,
                        RestClient.headersFrom(
                                new Pair<>(X_AUTH_TOKEN_HEADER, firstSessionToken),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        String.class
                );
        Assert.assertNotNull(helloResponse);
        Assert.assertNotNull(helloResponse.hasBody());
        Assert.assertEquals("hello!!!", helloResponse.getBody());

        //  Login with a same user again => second session
        loginResponse = getRestClient()
                .exchange(
                        USER_AUTHENTICATE_PATH,
                        HttpMethod.POST,
                        RestClient.headersFrom(
                                new Pair<>(X_AUTH_USERNAME_HEADER, user.getName()),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        Loginresponse.class);
        Assert.assertNotNull(loginResponse);
        Assert.assertNotNull(loginResponse.getHeaders());
        String secondSessionToken = loginResponse.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");
        Assert.assertFalse(secondSessionToken.equals(""));

        // Test there are two different sessions
        Assert.assertNotEquals(firstSessionToken, secondSessionToken);

        // Try to access user resource on behalf on second session
        helloResponse = getRestClient()
                .exchange(
                        USER_HELLO_PATH,
                        HttpMethod.GET,
                        RestClient.headersFrom(
                                new Pair<>(X_AUTH_TOKEN_HEADER, firstSessionToken),
                                new Pair<>(X_AUTH_PASSWORD_HEADER, user.getPassword())),
                        Optional.empty(),
                        String.class
                );
        Assert.assertNotNull(helloResponse);
        Assert.assertNotNull(helloResponse.hasBody());
        Assert.assertEquals("hello!!!", helloResponse.getBody());
    }
}

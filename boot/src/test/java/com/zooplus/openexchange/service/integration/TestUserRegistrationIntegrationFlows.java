package com.zooplus.openexchange.service.integration;

import com.zooplus.openexchange.mockers.TestApiController;
import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.database.domain.User;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
import com.zooplus.openexchange.service.security.SecurityConfig;
import com.zooplus.openexchange.starters.IntegrationStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_REGISTRATION_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.X_AUTH_TOKEN_HEADER;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestUserRegistrationIntegrationFlows extends TestApiController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserRegistration() throws Exception {
        // Request database
        final String userName = "ak";
        final String userPassword = "pwd";
        final String userEmail = "ak@zooplus.com";

        // Create request
        Registrationrequest req = new Registrationrequest();
        req.setName(userName);
        req.setPassword(userPassword);
        req.setEmail(userEmail);

        // Remember creation timestamp
        final long preUserCreationTimeStamp = System.currentTimeMillis();

        // Send request
        ResponseEntity<Registrationresponse> resp =
                client
                        .exchange(
                                provideEndPoint() + "/" + USER_REGISTRATION_PATH,
                                HttpMethod.POST,
                                new HttpEntity<>(req, adminHeaders),
                                Registrationresponse.class);

        // Analyze response
        Assert.assertNotNull(resp);
        Assert.assertNotNull(resp.getBody().getId());
        long newUserId = resp.getBody().getId();

        // Send the same request second time => HttpStatus.CONFLICT
        resp =
                client
                        .exchange(
                                provideEndPoint() + "/" + USER_REGISTRATION_PATH,
                                HttpMethod.POST,
                                new HttpEntity<>(req, adminHeaders),
                                Registrationresponse.class);

        // Analyze response
        Assert.assertNotNull(resp);
        Assert.assertEquals(resp.getStatusCode(), HttpStatus.CONFLICT);

        // Fetch user directly from repository by Id
        User user = userRepository.findOne(newUserId);
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), userName);
        Assert.assertTrue(passwordEncoder.matches(userPassword, user.getPassword()));
        Assert.assertEquals(user.getEmail(), userEmail);
        Assert.assertNotNull(user.getCreatedAt());
        Assert.assertTrue(preUserCreationTimeStamp < user.getCreatedAt().getTime());
        Assert.assertTrue(user.getEnabled());
        Assert.assertEquals(user.getRoles().size(), 1);
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("USER")));

        // Login with a regular user
        HttpHeaders clientHeader = new HttpHeaders();
        clientHeader.add(SecurityConfig.X_AUTH_USERNAME_HEADER, user.getName());
        clientHeader.add(SecurityConfig.X_AUTH_PASSWORD_HEADER, user.getPassword());
        ResponseEntity<Loginresponse> loginResp =
                client
                        .exchange(
                                provideEndPoint() + "/" + USER_AUTHENTICATE_PATH,
                                HttpMethod.POST,
                                new HttpEntity<>(clientHeader),
                                Loginresponse.class);

        // Analyze login response
        Assert.assertNotNull(loginResp);
        Assert.assertEquals(loginResp.getStatusCode(), HttpStatus.OK);
        Assert.assertTrue(loginResp.hasBody());
        String token = loginResp.getHeaders().toSingleValueMap().getOrDefault(X_AUTH_TOKEN_HEADER, "");

        Assert.assertNotNull(token);
    }
}

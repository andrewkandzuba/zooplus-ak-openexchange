package com.zooplus.openexchange.service.integration;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.Starter;
import com.zooplus.openexchange.service.controllers.v1.TestApiController;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_REGISTRATION_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Starter.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("development")
public class TestUserRegistrationIntegrationFlow extends TestApiController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserRegistration() throws Exception {
        // Request data
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

        // Fetch user directly from repository by Id
        User user = userRepository.findOne(resp.getBody().getId());
        Assert.assertNotNull(user);
        Assert.assertEquals(user.getName(), userName);
        Assert.assertTrue(passwordEncoder.matches(userPassword, user.getPassword()));
        Assert.assertEquals(user.getEmail(), userEmail);
        Assert.assertNotNull(user.getCreatedAt());
        Assert.assertTrue(preUserCreationTimeStamp < user.getCreatedAt().getTime());
        Assert.assertTrue(user.getEnabled());
        Assert.assertEquals(user.getRoles().size(), 1);
        Assert.assertTrue(user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("USER")));
    }
}

package com.zooplus.openexchange.service.controllers.v1.registration;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static com.zooplus.openexchange.service.controllers.v1.Constants.API;
import static com.zooplus.openexchange.service.controllers.v1.Constants.REGISTRATION;
import static com.zooplus.openexchange.service.controllers.v1.Constants.VERSION_1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(RegistrationControllerStater.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("development")
public class TestSubscriptionController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s%s";
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSubscription() throws Exception {
        // 0. Mock data and repository
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@zooplus.com");
        user.setPassword("password1234");
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByEmail("user1@zooplus.com")).thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);

        // 1. Make a request
        RestTemplate restTemplate = new RestTemplate();
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setEmail(user.getEmail());
        registrationrequest.setPassword(user.getPassword());

        // 2. Analyze response
        ResponseEntity<Registrationresponse> registrationResponse = restTemplate.postForEntity(
                String.format(TEST_ENDPOINT_TEMPLATE, port, "/" + API  + "/" + VERSION_1 + "/" + REGISTRATION),
                registrationrequest, Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(registrationResponse.getBody().getId().longValue(), 1L);
    }

    @Test(expected = org.springframework.web.client.HttpClientErrorException.class)
    public void testRegisterTwice() throws Exception {
        // 0. Mock data and repository
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@zooplus.com");
        user.setPassword("password1234");
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByEmail("user1@zooplus.com")).thenReturn(user);

        // 1. Make a request
        RestTemplate restTemplate = new RestTemplate();
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setEmail(user.getEmail());
        registrationrequest.setPassword(user.getPassword());

        // 2. Analyze response
        Mockito.when(userRepository.findByEmail("user1@zooplus.com")).thenReturn(user);
        ResponseEntity<Registrationresponse> registrationResponse = restTemplate.postForEntity(
                String.format(TEST_ENDPOINT_TEMPLATE, port, "/" + API  + "/" + VERSION_1 + "/" + REGISTRATION),
                registrationrequest, Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.CONFLICT);
    }
}

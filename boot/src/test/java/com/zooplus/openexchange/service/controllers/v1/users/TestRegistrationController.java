package com.zooplus.openexchange.service.controllers.v1.users;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.controllers.v1.TestApiController;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_REGISTRATION_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class TestRegistrationController extends TestApiController {
    private static final String TEST_ENDPOINT_TEMPLATE = "http://localhost:%s%s";
    @Value("${local.server.port}")
    private int port;
    @Autowired
    private UserRepository userRepository;


    @Before
    public void setUp() throws Exception {
        mockAdminAccess(userRepository);
    }

    @Test
    public void testUserRegistration() throws Exception {
        // Mock new data and repository
        User user = new User();
        user.setId(2L);
        user.setEmail("user1@zooplus.com");
        user.setPassword("password1234");
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByEmail("user1@zooplus.com")).thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(user)).thenReturn(user);

        // Prepare request data
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setEmail(user.getEmail());
        registrationrequest.setPassword(user.getPassword());
        HttpEntity<Registrationrequest> httpEntity = new HttpEntity<>(registrationrequest, headers);

        // Make a request
        ResponseEntity<Registrationresponse> registrationResponse = restTemplate.exchange(
                String.format(TEST_ENDPOINT_TEMPLATE, port, USER_REGISTRATION_PATH),
                HttpMethod.POST,
                httpEntity,
                Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(registrationResponse.getBody().getId().longValue(), 2L);
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
                String.format(TEST_ENDPOINT_TEMPLATE, port, USER_REGISTRATION_PATH),
                registrationrequest, Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.CONFLICT);
    }
}

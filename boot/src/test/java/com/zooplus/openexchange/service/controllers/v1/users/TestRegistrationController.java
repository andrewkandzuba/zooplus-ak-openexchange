package com.zooplus.openexchange.service.controllers.v1.users;

import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.controllers.v1.TestApiMockDbController;
import com.zooplus.openexchange.service.data.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_REGISTRATION_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class TestRegistrationController extends TestApiMockDbController {
    private final String userName = "user1";
    private final String userPassword = "someuserpassword";
    private final String userEmail = "user1@zooplus.com";

    @Test
    public void testUserRegistration() throws Exception {
        // Mock new data and repository
        User user = new User(userName, userPassword, userEmail);
        user.setId(generator.nextLong());
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);

        // Prepare request data
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setName(user.getName());
        registrationrequest.setPassword(user.getPassword());
        registrationrequest.setEmail(user.getEmail());
        HttpEntity<Registrationrequest> httpEntity = new HttpEntity<>(registrationrequest, adminHeaders);

        // Make a request
        ResponseEntity<Registrationresponse> registrationResponse =
                client
                        .exchange(
                                provideEndPoint() + "/" + USER_REGISTRATION_PATH,
                                HttpMethod.POST,
                                httpEntity,
                                Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(registrationResponse.getBody().getId(), user.getId());
    }

    @Test(expected = org.springframework.web.client.HttpClientErrorException.class)
    public void testRegisterTwice() throws Exception {
        // Mock new data and repository
        User user = new User(userName, userPassword, userEmail);
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(user);

        // Prepare request data
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setName(user.getName());
        registrationrequest.setPassword(user.getPassword());
        registrationrequest.setEmail(user.getEmail());

        // Make a request
        Mockito.when(userRepository.findByName(userName)).thenReturn(user);
        ResponseEntity<Registrationresponse> registrationResponse =
                client
                        .postForEntity(
                                provideEndPoint() + "/" + USER_REGISTRATION_PATH,
                                registrationrequest, Registrationresponse.class);
        Assert.assertNotNull(registrationResponse);
        Assert.assertEquals(registrationResponse.getStatusCode(), HttpStatus.CONFLICT);
    }
}

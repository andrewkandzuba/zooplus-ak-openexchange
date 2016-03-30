package com.zooplus.openexchange.service.controllers.v1.users;

import com.zooplus.openexchange.protocol.v1.Loginresponse;
import com.zooplus.openexchange.protocol.v1.Registrationrequest;
import com.zooplus.openexchange.protocol.v1.Registrationresponse;
import com.zooplus.openexchange.service.controllers.v1.ControllerStarter;
import com.zooplus.openexchange.service.controllers.v1.TestApiMockRepositoriesController;
import com.zooplus.openexchange.service.data.domain.Role;
import com.zooplus.openexchange.service.data.domain.User;
import com.zooplus.openexchange.service.security.SecurityConfig;
import com.zooplus.openexchange.service.utils.ApplicationUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_AUTHENTICATE_PATH;
import static com.zooplus.openexchange.service.controllers.v1.ApiController.USER_REGISTRATION_PATH;
import static com.zooplus.openexchange.service.security.SecurityConfig.X_AUTH_TOKEN_HEADER;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllerStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class TestAuthentication extends TestApiMockRepositoriesController {

    @Test
    public void testUserRegistration() throws Exception {
        // Mock new data
        String userName = "user1";
        String userPassword = "someuserpassword";
        String userEmail = "user1@zooplus.com";
        User user = new User(userName, userPassword, userEmail);
        user.setId(generator.nextLong());
        user.setRoles(Collections.singleton(new Role(generator.nextLong(), "USER")));

        // Mock repository's behaviors
        MockitoAnnotations.initMocks(this);
        Mockito.when(userRepository.findByName(user.getName())).thenReturn(null);
        Mockito.when(userRepository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);

        // Prepare request
        Registrationrequest registrationrequest = new Registrationrequest();
        registrationrequest.setName(user.getName());
        registrationrequest.setPassword(user.getPassword());
        registrationrequest.setEmail(user.getEmail());
        HttpEntity<Registrationrequest> httpEntity = new HttpEntity<>(registrationrequest, adminHeaders);

        // Send request and analyze response
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

        // Mock cache & repository
        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getName(), null, user.getRoles());
        userAuthenticationToken.setDetails(ApplicationUtils.nextToken());
        Mockito.when(tokenService.issue(user.getName(), user.getRoles())).thenReturn(userAuthenticationToken);
        Mockito.when(userRepository.findByNameAndPassword(user.getName(), user.getPassword())).thenReturn(user);

        // Authenticate and get token
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

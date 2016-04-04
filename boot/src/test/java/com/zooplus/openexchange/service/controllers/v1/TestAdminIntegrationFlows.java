package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.mockers.TestControllers;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.security.SecurityConfig;
import com.zooplus.openexchange.starters.ControllersStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.zooplus.openexchange.service.controllers.v1.ApiController.STATUS_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllersStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("controllers")
public class TestAdminIntegrationFlows extends TestControllers {
    @Test
    public void testAdminStatusPath() throws Throwable {
        // Make a request
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConfig.X_AUTH_TOKEN_HEADER, getAdminSessionToken());
        ResponseEntity<Status> response =
                getClient()
                        .exchange(
                                provideEndPoint() + "/" + STATUS_PATH,
                                HttpMethod.GET,
                                new HttpEntity<>(headers),
                                Status.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}
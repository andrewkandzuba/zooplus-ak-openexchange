package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.frontend.security.SecurityConfig;
import com.zooplus.openexchange.starters.ControllersStarter;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.zooplus.openexchange.service.frontend.controllers.v1.Version.STATUS_PATH;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllersStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("controllers")
public class TestAdminFlows extends TestMockedClient {
    @Test
    public void testAdminStatusPath() throws Throwable {
        // Make a request
        ResponseEntity<Status> response =
                getRestClient()
                        .exchange(
                                STATUS_PATH,
                                HttpMethod.GET,
                                RestClient.headersFrom(new Pair<>(SecurityConfig.X_AUTH_TOKEN_HEADER, getAdminSessionToken())),
                                Optional.empty(),
                                Status.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(response.getBody().getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}

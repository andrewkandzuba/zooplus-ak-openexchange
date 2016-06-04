package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.rest.v1.StatusResponse;
import com.zooplus.openexchange.starters.ControllersStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ControllersStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("controllers")
public class TestApplicationController extends TestMockedClient {
    @Test
    public void testInfoPath() throws Throwable {
        // Make a request
        ResponseEntity<StatusResponse> response =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + INFO_RESOURCE,
                                HttpMethod.GET,
                                RestClient.build(),
                                Optional.empty(),
                                StatusResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals("7935a0789a204973ab70b6f01458b4f3", response.getBody().getInstanceId());
    }

    @Test
    public void testHealthPath() throws Throwable {
        // Make a request
        ResponseEntity<?> response =
                getRestClient()
                        .exchange(
                                API_PATH_V1 + HEALTH_RESOURCE,
                                HttpMethod.GET,
                                RestClient.build(),
                                Optional.empty(),
                                StatusResponse.class);

        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

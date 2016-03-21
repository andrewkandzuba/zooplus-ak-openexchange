package com.zooplus.openexchange.service.controllers.v1.app;

import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.service.Starter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Starter.class)
@WebIntegrationTest
@ActiveProfiles("development")
public class TestAppController {
    @Value("${local.server.port}")
    int port;

    @Test
    public void testLifecycle() throws Throwable {
        RestTemplate restTemplate = new RestTemplate();
        Status response = restTemplate.getForObject(String.format("http://localhost:%s/v1/app/status", port), Status.class);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}

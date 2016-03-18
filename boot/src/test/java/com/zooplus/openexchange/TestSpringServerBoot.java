package com.zooplus.openexchange;

import com.zooplus.openexchange.protocol.v1.Status;
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
@WebIntegrationTest("server.port=0")
@ActiveProfiles("local")
public class TestSpringServerBoot {
    @Value("${local.server.port}")
    int port;

    @Test
    public void testLifecycle() throws Throwable {
        RestTemplate restTemplate = new RestTemplate();
        Status response = restTemplate.getForObject(String.format("http://localhost:%s/status", port), Status.class);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getInstanceId(), "7935a0789a204973ab70b6f01458b4f3");
    }
}

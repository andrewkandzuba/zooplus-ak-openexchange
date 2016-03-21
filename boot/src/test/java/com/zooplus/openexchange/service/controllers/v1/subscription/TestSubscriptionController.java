package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.domain.Subscriber;
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
@SpringApplicationConfiguration(SubscriptionControllerStater.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("development")
public class TestSubscriptionController {
    @Value("${local.server.port}")
    int port;

    @Test
    public void testSubscription() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail("RS@AK.COM");
        subscriber.setPassword("test1");
        Subscriber response = restTemplate.postForObject(
                String.format("http://localhost:%s/%s", port, SubscriptionController.SUBSCRIBES_PATH), subscriber, Subscriber.class);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId().longValue(), 1L);

    }
}

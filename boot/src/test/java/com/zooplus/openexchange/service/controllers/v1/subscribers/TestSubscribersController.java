package com.zooplus.openexchange.service.controllers.v1.subscribers;

import com.zooplus.openexchange.service.Starter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Starter.class)
@WebIntegrationTest
@ActiveProfiles("development")
public class TestSubscribersController {
    @Value("${local.server.port}")
    int port;

    @Test
    public void testSubscription() throws Exception {

    }
}

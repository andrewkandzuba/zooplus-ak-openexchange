package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.starters.ApiStarter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.integration.http.inbound.HttpRequestHandlingMessagingGateway;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ApiStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("currencyplay")
public class TestCurrencyPlayEndpoint {
    @Test
    public void testCurrencies() throws Exception {
        HttpRequestHandlingMessagingGateway httpRequestHandlingMessagingGateway = new HttpRequestHandlingMessagingGateway();
    }
}

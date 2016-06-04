package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.starters.IntegrationStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestEurekaDiscovery {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Test
    public void testDiscoveryChannel() throws Exception {
        Assert.assertNotNull(discoveryClient);
        int numberOfTries = 10;
        List<String> services = discoveryClient.getServices();
        while (numberOfTries-- > 0 && services.isEmpty()) {
            Thread.sleep(1000);
            services = discoveryClient.getServices();
        }
        services.forEach(System.out::println);
    }
}

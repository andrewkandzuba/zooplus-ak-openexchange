package com.zooplus.openexchange;

import com.zooplus.openexchange.integrations.gateways.StatusGateway;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.services.StatusService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestIntegrationFramework.class)
@WebIntegrationTest
@SpringBootApplication
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class TestIntegrationFramework {

    @Autowired
    StatusGateway request;

    @Test
    public void testIntegration() {
        Status s = request.getStatus();
        Assert.assertNotNull(s);
        Assert.assertTrue(s.getInstanceId().equals("id-1"));
        Assert.assertTrue(s.getHost().equals("localhost"));
        Assert.assertTrue(s.getPort() == 8888);
    }

    @Bean
    StatusService getStatusService() {
        return () -> {
            Status s = new Status();
            s.setHost("localhost");
            s.setInstanceId("id-1");
            s.setPort(8888);
            return s;
        };
    }
}

package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.integrations.gateways.StatusGateway;
import com.zooplus.openexchange.protocol.v1.Status;
import com.zooplus.openexchange.starters.LogicStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(LogicStarter.class)
@ActiveProfiles("logic")
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
}

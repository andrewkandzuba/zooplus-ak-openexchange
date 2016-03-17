package com.zooplus.openexchange;

import com.zooplus.openexchange.boot.Instance;
import com.zooplus.openexchange.protocol.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Starter.class)
@WebIntegrationTest
public class TestSpringServerBoot {
    @Autowired
    private ApplicationContext ctx;

    @Test
    public void testLifecycle() throws Throwable {
        Instance i = ctx.getBean(Instance.class);
        RestTemplate restTemplate = new RestTemplate();
        Status response = restTemplate.getForObject(String.format("http://%s/status", i.getAddress()), Status.class);
        Assert.assertNotNull(response);
        Assert.assertEquals(i.getAddress().getHostName(), response.getIp());
        Assert.assertEquals(i.getAddress().getPort(), response.getPort().intValue());
        Assert.assertEquals(i.getId(), response.getId());
    }
}

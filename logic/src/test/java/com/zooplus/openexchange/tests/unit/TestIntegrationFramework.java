package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.starters.LogicStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(LogicStarter.class)
@ActiveProfiles("logic")
public class TestIntegrationFramework {
    @Autowired
    CurrenciesGateway request;

    @Test
    public void testIntegration() {
        List<Currency> s = request.getCurrencies();
        Assert.assertNotNull(s);
        Assert.assertTrue(s.size() > 0);
    }
}

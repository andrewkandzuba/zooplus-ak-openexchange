package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.starters.LogicStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(LogicStarter.class)
@ActiveProfiles("logic")
public class TestIntegrationFramework {
    @Autowired
    CurrenciesGateway gateway;

    @Test
    public void testSupportedCadencesList() {
        List<Currency> currencies = gateway.getCurrenciesList();
        Assert.assertNotNull(currencies);
        Assert.assertTrue(currencies.size() > 0);
    }

    @Test
    public void testRates() throws Exception {
        List<Rate> rates = gateway.getRates(Date.from(Instant.now()), Optional.of(new Currency("USD", "United States Dollar")));
        Assert.assertNotNull(rates);
        Assert.assertTrue(rates.size() > 0);
        Assert.assertEquals(rates.get(0).getAlternative().getCode(), "EUR");
    }
}

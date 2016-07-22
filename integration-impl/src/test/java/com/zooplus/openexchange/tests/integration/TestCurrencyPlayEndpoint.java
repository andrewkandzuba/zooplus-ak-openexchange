package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import com.zooplus.openexchange.starters.IntegrationTestStarter;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationTestStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestCurrencyPlayEndpoint {
    @Autowired
    private CurrencyLayerApi gateway;

    @Test
    public void testCurrencyLayerCurrenciesList() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        gateway.getCurrenciesList(10).addCallback(
                currencies -> {
                    Assert.assertTrue(currencies.getCurrencies().size() > 0);
                    replied.compareAndSet(false, true);
                    latch.countDown();
                }, t -> {
                    latch.countDown();
                    t.printStackTrace();
                });

        latch.await();
        Assert.assertTrue(replied.get());
    }

    @Test
    public void testCurrencyLayerHistoricalQuotes() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        gateway.getHistoricalQuotes("USD,EUR", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"))
                .addCallback(
                        historicalQuotesResponse -> {
                            Assert.assertTrue(historicalQuotesResponse.getQuotes().size() > 0);
                            replied.compareAndSet(false, true);
                            latch.countDown();
                        },
                        t -> {
                            latch.countDown();
                            t.printStackTrace();
                        });
        Assert.assertTrue(replied.get());
    }
}

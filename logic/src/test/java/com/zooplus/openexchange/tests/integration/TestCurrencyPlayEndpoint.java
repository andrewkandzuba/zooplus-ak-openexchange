package com.zooplus.openexchange.tests.integration;

import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(IntegrationTestStarter.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("integration")
public class TestCurrencyPlayEndpoint {
    @Autowired
    private CurrencyLayerApiGateway gateway;

    @Test
    public void testCurrencyLayerCurrenciesList() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        gateway.getCurrenciesList(new CurrencyListRequest()).addCallback(
                currencies -> {
                    Assert.assertTrue(currencies.getCurrencies().size() > 0);
                    replied.compareAndSet(false, true);
                    latch.countDown();
                }, Throwable::printStackTrace);

        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(replied.get());
    }

    @Test
    public void testCurrencyLayerHistoricalQuotes() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean replied = new AtomicBoolean(false);
        HistoricalQuotesRequest request = new HistoricalQuotesRequest();
        request.setCurrencyCode("USD");
        request.setExchangeDate(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-mm-dd"));
        gateway.getHistoricalQuotes(request)
                .addCallback(
                        historicalQuotesResponse -> {
                            Assert.assertTrue(historicalQuotesResponse.getRates().size() > 0);
                            replied.compareAndSet(false, true);
                            latch.countDown();
                        },
                        Throwable::printStackTrace);
        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(replied.get());
    }
}

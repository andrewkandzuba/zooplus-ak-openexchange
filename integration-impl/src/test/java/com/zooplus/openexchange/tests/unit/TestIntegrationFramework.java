package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.integrations.gateways.BorderConditionsGateway;
import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.starters.UnitTestStarter;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(UnitTestStarter.class)
@ActiveProfiles("api")
public class TestIntegrationFramework {
    @Autowired
    private CurrencyLayerApiGateway currencyLayerApiGateway;
    @Autowired
    private BorderConditionsGateway borderConditionsGateway;

    @Test
    public void testCurrencyList() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean stateSuccess = new AtomicBoolean(false);
        AtomicBoolean stateError = new AtomicBoolean(false);
        currencyLayerApiGateway.getCurrenciesList(new CurrencyListRequest())
                .addCallback(
                        currencies -> {
                            Assert.assertTrue(currencies.getCurrencies().size() > 0);
                            stateSuccess.compareAndSet(false, true);
                            latch.countDown();
                        }, throwable -> {
                            throwable.printStackTrace();
                            stateError.compareAndSet(false, true);
                            latch.countDown();
                        });
        latch.await(5000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(stateSuccess.get());
        Assert.assertFalse(stateError.get());
    }

    @Test
    public void testHistoricalQuotes() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean stateSuccess = new AtomicBoolean(false);
        AtomicBoolean stateError = new AtomicBoolean(false);
        HistoricalQuotesRequest request = new HistoricalQuotesRequest();
        request.setCurrencyCode("USD");
        request.setExchangeDate(DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd"));
        currencyLayerApiGateway.getHistoricalQuotes(request)
                .addCallback(
                        historicalQuotes -> {
                            Assert.assertEquals(historicalQuotes.getExchangeRates().get(0).getTo(), "EUR");
                            Assert.assertEquals(historicalQuotes.getExchangeDate(), "2016-04-28");
                            stateSuccess.compareAndSet(false, true);
                            latch.countDown();
                        }, throwable -> {
                            throwable.printStackTrace();
                            stateError.compareAndSet(false, true);
                            latch.countDown();
                        });
        ;
        latch.await(5000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(stateSuccess.get());
        Assert.assertFalse(stateError.get());
    }

    @Test
    public void testExceptionHandling() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean stateError = new AtomicBoolean(false);
        borderConditionsGateway.throwNullPointerException(new NullPointerExceptionMessage())
                .addCallback(
                        aVoid -> {
                        },
                        throwable -> {
                            throwable.printStackTrace();
                            stateError.compareAndSet(false, true);
                            latch.countDown();
                        });
        ;
        latch.await(5000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(stateError.get());
    }

    @Test
    public void testCachableInvocations() throws Exception {
        String id = "message1";
        List<String> responses = new ArrayList<>();
        responses.add(borderConditionsGateway.cachableInvocations(id).get());
        responses.add(borderConditionsGateway.cachableInvocations(id).get());
        Assert.assertTrue(responses.size() == 2);
        Assert.assertTrue(responses.get(0).equals(responses.get(1)));
    }
}

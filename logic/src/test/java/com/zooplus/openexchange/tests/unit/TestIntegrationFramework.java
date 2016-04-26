package com.zooplus.openexchange.tests.unit;

import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import com.zooplus.openexchange.starters.UnitTestStarter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(UnitTestStarter.class)
@ActiveProfiles("api")
public class TestIntegrationFramework {
    @Autowired
    private CurrencyLayerApiGateway currencyLayerApiGateway;

    @Test
    public void testSupportedCurrenciesList() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean stateSuccess = new AtomicBoolean(false);
        AtomicBoolean stateError = new AtomicBoolean(false);
        ListenableFuture<CurrencyListResponse> reply = currencyLayerApiGateway.getCurrenciesList(new CurrencyListRequest());
        reply.addCallback(currencies -> {
            try {
                CurrencyListResponse response = reply.get();
                Assert.assertNotNull(response);
                Assert.assertTrue(response.getCurrencies().size() > 0);
                stateSuccess.compareAndSet(false, true);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                stateError.compareAndSet(false, true);
            }
            latch.countDown();
        }, throwable -> {
            throwable.printStackTrace();
            stateError.compareAndSet(false, true);
            latch.countDown();
        });
        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(stateSuccess.get());
        Assert.assertFalse(stateError.get());
    }

    @Test
    public void testRates() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean stateSuccess = new AtomicBoolean(false);
        AtomicBoolean stateError = new AtomicBoolean(false);
        HistoricalQuotesRequest request = new HistoricalQuotesRequest();
        request.setCurrencyCode("USD");
        ListenableFuture<HistoricalQuotesResponse> reply = currencyLayerApiGateway.getHistoricalQuotes(request);
        reply.addCallback(historicalQuotes -> {
            try {
                HistoricalQuotesResponse historicalQuotesResponse = reply.get();
                Assert.assertNotNull(historicalQuotesResponse);
                Assert.assertEquals(historicalQuotesResponse.getRates().get(0).getAlternative().getCode(), "EUR");
                stateSuccess.compareAndSet(false, true);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                stateError.compareAndSet(false, true);
            }
            latch.countDown();
        }, throwable -> {
            throwable.printStackTrace();
            stateError.compareAndSet(false, true);
            latch.countDown();
        });
        ;
        latch.await(3000, TimeUnit.MILLISECONDS);
        Assert.assertTrue(stateSuccess.get());
        Assert.assertFalse(stateError.get());
    }
}

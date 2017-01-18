package com.zooplus.openexchange.services.external.currencylayer.impl;

import com.zooplus.openexchange.protocol.integration.Currencies;
import com.zooplus.openexchange.protocol.integration.Quotes;
import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class CurrencyLayerApiService implements CurrencyLayerApi {
    private static final Logger logger = LoggerFactory.getLogger("CurrencyLayerApiService");
    @Value("${currencyplayer.api.accesskey}")
    private String accessKey;

    @Autowired
    private CurrencyLayerDAO dao;

    @Override
    public ListenableFuture<Currencies> getCurrenciesList(int top) {
        ListenableFuture<Currencies> f;
        try {
            f = AsyncResult.forValue(dao.getCurrenciesListCached(top));
        } catch (Throwable t) {
            f = AsyncResult.forExecutionException(t);
        }
        return f;
    }

    @Override
    public ListenableFuture<Quotes> getHistoricalQuotes(String currencyCode, String exchangeDate) {
        ListenableFuture<Quotes> f;
        try {
            f = AsyncResult.forValue(dao.getHistoricalQuotes(currencyCode, exchangeDate));
        } catch (Throwable t) {
            f = AsyncResult.forExecutionException(t);
        }
        return f;
    }
}

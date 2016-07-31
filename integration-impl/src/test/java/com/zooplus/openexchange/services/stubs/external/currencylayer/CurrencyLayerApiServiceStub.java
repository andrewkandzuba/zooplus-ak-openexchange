package com.zooplus.openexchange.services.external.currencylayer.stub;

import com.zooplus.openexchange.protocol.ws.v1.*;
import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CurrencyLayerApiServiceStub implements CurrencyLayerApi {
    @Override
    public ListenableFuture<Currencies> getCurrenciesList(int top) {
        Currencies currencies = new Currencies();
        currencies.setCurrencies(Collections.singletonMap("USD", "United States Dollar"));
        return new AsyncResult<>(currencies);
    }

    @Override
    public ListenableFuture<Quotes> getHistoricalQuotes(String currencyCode, String exchangeDate) {
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("EUR", 1.222);
        exchangeRates.put("UAH", 26.3);
        Quotes quotes = new Quotes();
        quotes.setQuotes(exchangeRates);
        return new AsyncResult<>(quotes);
    }
}

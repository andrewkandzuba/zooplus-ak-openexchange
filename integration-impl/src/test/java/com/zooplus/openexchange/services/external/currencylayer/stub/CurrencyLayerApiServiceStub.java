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
    public ListenableFuture<CurrencyListResponse> getCurrenciesList(CurrencyListRequest request) {
        Currencies currencies = new Currencies();
        currencies.setCurrencies(Collections.singletonMap("USD", "United States Dollar"));
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(currencies);
        return new AsyncResult<>(response);
    }

    @Override
    public ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(HistoricalQuotesRequest request) {
        Map<String, Double> exchangeRates = new HashMap<>();
        exchangeRates.put("EUR", 1.222);
        exchangeRates.put("UAH", 26.3);
        Quotes quotes = new Quotes();
        quotes.setQuotes(exchangeRates);
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setExchangeDate("2016-04-28");
        response.setQuotes(quotes);
        return new AsyncResult<>(response);
    }
}

package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApi;
import com.zooplus.openexchange.protocol.ws.v1.*;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Arrays;
import java.util.Collections;

@Service
public class CurrencyLayerApiServiceStub implements CurrencyLayerApi {
    @Override
    public ListenableFuture<CurrencyListResponse> getCurrenciesList(CurrencyListRequest request) {
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setDescription("United States Dollar");
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(Collections.singletonList(currency));
        return new AsyncResult<>(response);
    }

    @Override
    public ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(HistoricalQuotesRequest request) {
        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setFrom("USD");
        exchangeRate1.setTo("EUR");
        exchangeRate1.setRate(1.222);

        ExchangeRate exchangeRate2 = new ExchangeRate();
        exchangeRate2.setFrom("USD");
        exchangeRate2.setTo("UAH");
        exchangeRate2.setRate(26.3);

        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setExchangeDate("2016-04-28");
        response.setExchangeRates(
                Arrays.asList(exchangeRate1, exchangeRate2));
        return new AsyncResult<>(response);
    }
}

package com.zooplus.openexchange.services.external.currencylayer.impl;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.ws.v1.*;
import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.PostConstruct;

import static com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApiConstants.*;

@Service
public class CurrencyLayerApiService implements CurrencyLayerApi {
    @Value("${currencyplayer.api.accesskey}")
    private String accessKey;
    private RestClient restClient;

    @Override
    public ListenableFuture<CurrencyListResponse> getCurrenciesList(CurrencyListRequest request) {
        ResponseEntity<Currencies> response = restClient.exchange(
                LIST_METHOD + "?"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1",
                HttpMethod.GET,
                RestClient.build(),
                Currencies.class);

        CurrencyListResponse currencyListResponse  = new CurrencyListResponse();
        currencyListResponse.setCurrencies(response.getBody());

        return new AsyncResult<>(currencyListResponse);
    }

    @Override
    public ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(HistoricalQuotesRequest request) {
        ResponseEntity<Quotes> response = restClient.exchange(
                String.format(HISTORICAL_METHOD + "?"
                        + CURRENCIES_PARAM + "=%s" + "&"
                        + DATE_PARAM + "=%s" + "&"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1", request.getCurrencyCode(), request.getExchangeDate()),
                HttpMethod.GET,
                RestClient.build(),
                Quotes.class);

        HistoricalQuotesResponse historicalQuotesResponse = new HistoricalQuotesResponse();
        historicalQuotesResponse.setExchangeDate(response.getBody().getDate());
        historicalQuotesResponse.setQuotes(response.getBody());

        return new AsyncResult<>(historicalQuotesResponse);
    }

    @PostConstruct
    private void init() {
        restClient = new RestClient(BASE_URL, 80);
    }
}

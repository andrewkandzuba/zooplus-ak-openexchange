package com.zooplus.openexchange.integrations.services;

import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApi;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class CurrencyLayerApiService implements CurrencyLayerApi {
    @Override
    public ListenableFuture<CurrencyListResponse> getCurrenciesList(CurrencyListRequest request) {
        return null;
    }

    @Override
    public ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(HistoricalQuotesRequest request) {
        return null;
    }
}

package com.zooplus.openexchange.services.external.currencylayer.api;

import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.util.concurrent.ListenableFuture;

public interface CurrencyLayerApi {
    ListenableFuture<CurrencyListResponse> getCurrenciesList(CurrencyListRequest request);
    ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(HistoricalQuotesRequest request);
}

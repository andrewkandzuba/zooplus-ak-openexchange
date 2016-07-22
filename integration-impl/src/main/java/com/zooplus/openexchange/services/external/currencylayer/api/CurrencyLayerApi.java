package com.zooplus.openexchange.services.external.currencylayer.api;

import com.zooplus.openexchange.protocol.ws.v1.Currencies;
import com.zooplus.openexchange.protocol.ws.v1.Quotes;
import org.springframework.util.concurrent.ListenableFuture;

public interface CurrencyLayerApi {
    ListenableFuture<Currencies> getCurrenciesList(int top);
    ListenableFuture<Quotes> getHistoricalQuotes(String currencyCode, String exchangeDate);
}

package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES;
import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.IN_DIRECT_CURRENCYLAYER_LIST;

@MessagingGateway
public interface CurrencyLayerApiGateway {
    @Gateway(requestChannel = IN_DIRECT_CURRENCYLAYER_LIST)
    ListenableFuture<CurrencyListResponse> getCurrenciesList(@Payload CurrencyListRequest request);

    @Gateway(requestChannel = IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES)
    ListenableFuture<HistoricalQuotesResponse> getHistoricalQuotes(@Payload HistoricalQuotesRequest request);
}

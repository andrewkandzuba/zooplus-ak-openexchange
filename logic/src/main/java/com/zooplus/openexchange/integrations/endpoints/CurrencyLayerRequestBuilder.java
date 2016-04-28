package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

@MessageEndpoint
public class CurrencyLayerRequestBuilder {

    public static final String IN_API_CURRENCYLAYER_LIST = "in.api.currencylayer.list";
    public static final String IN_API_CURRENCYLAYER_HISTORICALQUOTES = "in.api.currencylayer.historicalquotes";

    @Transformer(inputChannel = CurrencyLayerApiGateway.IN_DIRECT_CURRENCYLAYER_LIST, outputChannel = CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_LIST)
    public CurrencyListRequest buildCurrencyListRequest(Message<CurrencyListRequest> msg) {
        return msg.getPayload();
    }

    @Transformer(inputChannel = CurrencyLayerApiGateway.IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES, outputChannel = CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesRequest buildCurrencyRatesRequest(Message<HistoricalQuotesRequest> msg) {
        HistoricalQuotesRequest request = msg.getPayload();
        Assert.notNull(request.getCurrencyCode());
        Assert.notNull(request.getExchangeDate());
        return msg.getPayload();
    }
}

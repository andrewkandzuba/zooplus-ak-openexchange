package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.*;

@MessageEndpoint
public class CurrencyLayerRequestBuilder {

    @Transformer(inputChannel = IN_DIRECT_CURRENCYLAYER_LIST, outputChannel = IN_API_CURRENCYLAYER_LIST)
    public CurrencyListRequest buildCurrencyListRequest(Message<CurrencyListRequest> msg) {
        return msg.getPayload();
    }

    @Transformer(inputChannel = IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES, outputChannel = IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesRequest buildCurrencyRatesRequest(Message<HistoricalQuotesRequest> msg) {
        HistoricalQuotesRequest request = msg.getPayload();
        Assert.notNull(request.getCurrencyCode());
        Assert.notNull(request.getExchangeDate());
        return msg.getPayload();
    }
}

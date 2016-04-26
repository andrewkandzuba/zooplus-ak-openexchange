package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerApiIntegrationConfiguration.IN_API_CURRENCYLAYER;
import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerApiIntegrationConfiguration.IN_DIRECT_CURRENCYLAYER;

@Component
public class CurrenciesRequestBuilder {

    @Transformer(inputChannel = IN_DIRECT_CURRENCYLAYER, outputChannel = IN_API_CURRENCYLAYER)
    public CurrencyListRequest buildCurrencyListRequest(Message<CurrencyListRequest> msg) {
        return msg.getPayload();
    }

    @Transformer(inputChannel = IN_DIRECT_CURRENCYLAYER, outputChannel = IN_API_CURRENCYLAYER)
    public HistoricalQuotesRequest buildCurrencyRatesRequest(Message<HistoricalQuotesRequest> msg) {
        return msg.getPayload();
    }
}

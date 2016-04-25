package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.IN_DIRECT_CURRENCIES_LIST;
import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.IN_REST_CURRENCIES_LIST;
import static com.zooplus.openexchange.integrations.configurations.CurrencyRatesIntegrationConfiguration.IN_DIRECT_CURRENCIES_RATES;
import static com.zooplus.openexchange.integrations.configurations.CurrencyRatesIntegrationConfiguration.IN_REST_CURRENCIES_RATES;

@Component
public class CurrenciesRequestBuilder {

    @Transformer(inputChannel = IN_DIRECT_CURRENCIES_LIST, outputChannel = IN_REST_CURRENCIES_LIST)
    public CurrenciesListRequest buildCurrencyListRequest(Message<?> msg) {
        return new CurrenciesListRequest();
    }

    @Transformer(inputChannel = IN_DIRECT_CURRENCIES_RATES, outputChannel = IN_REST_CURRENCIES_RATES)
    public HistoricalQuotesRequest buildCurrencyRatesRequest(Message<?> msg) {
        return new HistoricalQuotesRequest();
    }
}

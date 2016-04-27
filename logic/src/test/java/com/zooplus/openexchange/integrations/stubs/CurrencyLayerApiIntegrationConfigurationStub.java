package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Arrays;
import java.util.Collections;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.IN_API_CURRENCYLAYER_HISTORICALQUOTES;
import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.IN_API_CURRENCYLAYER_LIST;
import static com.zooplus.openexchange.integrations.gateways.BorderConditionsGateway.IN_DIRECT_CURRENCYLAYER;

@MessageEndpoint
public class CurrencyLayerApiIntegrationConfigurationStub {
    @ServiceActivator(inputChannel = IN_API_CURRENCYLAYER_LIST)
    public CurrencyListResponse getCurrencyList(CurrencyListRequest request) {
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(Collections.singletonList(new Currency("USD", "United States Dollar")));
        return response;
    }

    @ServiceActivator(inputChannel = IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesResponse getHistoricalQuotes(HistoricalQuotesRequest request) {
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setRates(
                Arrays.asList(
                        new Rate(
                                new Currency("USD", "United States Dollar"),
                                new Currency("EUR", "European euro"),
                                1.222),
                        new Rate(
                                new Currency("USD", "United States Dollar"),
                                new Currency("UAH", "Ukrainian hryvna"),
                                26.3)));
        return response;
    }

    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCYLAYER)
    public Void throwNullPointerException(NullPointerExceptionMessage message){
        throw new NullPointerException("The methods always throws NullPointerException");
    }
}

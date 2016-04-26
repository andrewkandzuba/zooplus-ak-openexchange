package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerApiIntegrationConfiguration.IN_DIRECT_CURRENCYLAYER;

@Component
public class CurrenciesGatewayStub {
    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCYLAYER)
    public CurrencyListResponse getCurrencies(CurrencyListRequest request) {
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(Collections.singletonList(new Currency("USD", "United States Dollar")));
        return response;
    }

    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCYLAYER)
    public HistoricalQuotesResponse getRates(HistoricalQuotesRequest request) {
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setRates(
                Arrays.asList(
                        new Rate(
                                new Currency("USD", "United States Dollar"),
                                new Currency("EUR", "European euro"),
                                BigDecimal.valueOf(1.222)),
                        new Rate(
                                new Currency("USD", "United States Dollar"),
                                new Currency("UAH", "Ukrainian hryvna"),
                                BigDecimal.valueOf(26.3))));
        return response;
    }
}

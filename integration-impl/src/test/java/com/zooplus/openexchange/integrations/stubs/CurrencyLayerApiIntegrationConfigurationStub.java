package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import com.zooplus.openexchange.protocol.ws.v1.*;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static com.zooplus.openexchange.integrations.endpoints.CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_HISTORICALQUOTES;
import static com.zooplus.openexchange.integrations.endpoints.CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_LIST;
import static com.zooplus.openexchange.integrations.gateways.BorderConditionsGateway.IN_DIRECT_CURRENCYLAYER_CACHE;
import static com.zooplus.openexchange.integrations.gateways.BorderConditionsGateway.IN_DIRECT_CURRENCYLAYER_EXCEPTIONS;

@MessageEndpoint
public class CurrencyLayerApiIntegrationConfigurationStub {
    @ServiceActivator(inputChannel = IN_API_CURRENCYLAYER_LIST)
    public CurrencyListResponse getCurrencyList(CurrencyListRequest request) {
        Currency currency = new Currency();
        currency.setCode("USD");
        currency.setDescription("United States Dollar");
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(Collections.singletonList(currency));
        return response;
    }

    @ServiceActivator(inputChannel = IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesResponse getHistoricalQuotes(HistoricalQuotesRequest request) {
        ExchangeRate exchangeRate1 = new ExchangeRate();
        exchangeRate1.setFrom("USD");
        exchangeRate1.setTo("EUR");
        exchangeRate1.setRate(1.222);

        ExchangeRate exchangeRate2 = new ExchangeRate();
        exchangeRate2.setFrom("USD");
        exchangeRate2.setTo("UAH");
        exchangeRate2.setRate(26.3);

        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setExchangeDate("2016-04-28");
        response.setExchangeRates(
                Arrays.asList(exchangeRate1, exchangeRate2));
        return response;
    }

    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCYLAYER_EXCEPTIONS)
    public Void throwNullPointerException(NullPointerExceptionMessage message) {
        throw new NullPointerException("The methods always throws NullPointerException");
    }

    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCYLAYER_CACHE)
    public String cachableInvocations(String message) {
        return UUID.randomUUID().toString();
    }
}

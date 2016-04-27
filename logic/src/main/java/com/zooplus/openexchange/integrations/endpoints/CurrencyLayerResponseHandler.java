package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.protocol.integration.v1.Quotes;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerChannelsConfiguration.OUT_SUCCESS_PUBSUB_CURRENCYLAYER;

@MessageEndpoint
public class CurrencyLayerResponseHandler {

    @ServiceActivator(inputChannel = OUT_SUCCESS_PUBSUB_CURRENCYLAYER)
    public CurrencyListResponse getCurrencyList(Message<Currencies> msg) {
        List<Currency> list = msg.getPayload()
                .getCurrencies()
                .entrySet()
                .stream()
                .map(entry -> new Currency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(list);
        return response;
    }

    @ServiceActivator(inputChannel = OUT_SUCCESS_PUBSUB_CURRENCYLAYER)
    public HistoricalQuotesResponse getHistoricalQuotes(Message<Quotes> msg) {
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setRates(Collections.EMPTY_LIST);
        return response;
    }
}

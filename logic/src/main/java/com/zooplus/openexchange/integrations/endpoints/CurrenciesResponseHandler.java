package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerApiIntegrationConfiguration.OUT_PUBSUB_CURRENCYLAYER;

@Component
public class CurrenciesResponseHandler {

    @ServiceActivator(inputChannel = OUT_PUBSUB_CURRENCYLAYER)
    public CurrencyListResponse getResponse(Message<Currencies> msg) {
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
}

package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.OUT_PUBSUB_CURRENCIES_LIST;

@Component
public class CurrenciesResponseHandler {

    @ServiceActivator(inputChannel = OUT_PUBSUB_CURRENCIES_LIST)
    public List<Currency> getResponse(Message<Currencies> msg) {
        List<Currency> list = msg.getPayload()
                .getCurrencies()
                .entrySet()
                .stream()
                .map(entry -> new Currency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        return list;
    }
}

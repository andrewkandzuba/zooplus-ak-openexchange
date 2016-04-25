package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.database.domain.Currency;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.IN_DIRECT_CURRENCIES_LIST;

@MessagingGateway(defaultRequestChannel = IN_DIRECT_CURRENCIES_LIST)
public interface CurrencyListGateway {
    @Payload("new java.util.Date()")
    ListenableFuture<List<Currency>> getCurrenciesList();
}

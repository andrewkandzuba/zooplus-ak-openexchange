package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.database.domain.Currency;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;

@MessagingGateway
public interface CurrenciesGateway {
    String CURRENCIES_GATEWAY_CHANNEL = "in.currencies.channel";
    @Gateway(requestChannel = CURRENCIES_GATEWAY_CHANNEL)
    @Payload("new java.util.Date()")
    List<Currency> getCurrencies();
}

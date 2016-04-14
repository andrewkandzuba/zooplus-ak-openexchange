package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@MessagingGateway
public interface CurrenciesGateway {
    String CURRENCIES_INBOUND_CHANNEL = "in.channel.currencies";
    String CURRENCIES_LIST_INBOUND_CHANNEL = "in.channel.currencies.list";
    String CURRENCIES_RATE_INBOUND_CHANNEL = "in.channel.currencies.rates";
    String RATES_BASIC_CURRENCY_HEADER = "in.channel.currencies.rates.basic.currency";

    @Gateway(requestChannel = CURRENCIES_LIST_INBOUND_CHANNEL)
    @Payload("new java.util.Date()")
    List<Currency> getCurrenciesList();

    @Gateway(requestChannel = CURRENCIES_RATE_INBOUND_CHANNEL)
    List<Rate> getRates(@Payload Date date, @Header(CurrenciesGateway.RATES_BASIC_CURRENCY_HEADER) Optional<Currency> basic);
}

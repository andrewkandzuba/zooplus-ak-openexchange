package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@MessagingGateway
public interface CurrenciesGateway {
    String INBOUND_CHANNEL_CURRENCIES_LIST = "in.channel.currencies.list";
    String OUTBOUND_CHANNEL_CURRENCIES_LIST = "out.channel.currencies.list";
    String INBOUND_CHANNEL_CURRENCIES_RATE = "in.channel.currencies.rates";
    String OUTBOUND_CHANNEL_CURRENCIES_RATE = "out.channel.currencies.rates";

    String RATES_BASIC_CURRENCY_HEADER = "x-currency-basic-rates-header";

    @Gateway(requestChannel = INBOUND_CHANNEL_CURRENCIES_LIST)
    @Payload("new java.util.Date()")
    ListenableFuture<List<Currency>> getCurrenciesList();

    @Gateway(requestChannel = INBOUND_CHANNEL_CURRENCIES_RATE)
    ListenableFuture<List<Rate>> getRates(@Payload Date date, @Header(CurrenciesGateway.RATES_BASIC_CURRENCY_HEADER) Optional<Currency> basic);
}

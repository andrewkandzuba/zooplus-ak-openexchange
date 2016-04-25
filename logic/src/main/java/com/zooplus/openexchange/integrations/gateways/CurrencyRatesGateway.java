package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;

import static com.zooplus.openexchange.integrations.configurations.CurrencyRatesIntegrationConfiguration.IN_DIRECT_CURRENCIES_RATES;

@MessagingGateway(defaultRequestChannel = IN_DIRECT_CURRENCIES_RATES)
public interface CurrencyRatesGateway {
    ListenableFuture<List<Rate>> getRates(@Payload Currency basic);
}

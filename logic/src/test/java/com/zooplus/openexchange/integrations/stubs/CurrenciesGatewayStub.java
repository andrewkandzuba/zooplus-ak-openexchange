package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.IN_DIRECT_CURRENCIES_LIST;
import static com.zooplus.openexchange.integrations.configurations.CurrencyRatesIntegrationConfiguration.IN_DIRECT_CURRENCIES_RATES;

@Component
public class CurrenciesGatewayStub {
    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCIES_LIST)
    public List<Currency> getCurrencies() {
        return Collections.singletonList(new Currency("USD", "United States Dollar"));
    }

    @ServiceActivator(inputChannel = IN_DIRECT_CURRENCIES_RATES)
    public List<Rate> getRates(@Payload Currency basic) {
        return Arrays.asList(
                new Rate(
                        new Currency("USD", "United States Dollar"),
                        new Currency("EUR", "European euro"),
                        BigDecimal.valueOf(1.222)),
                new Rate(
                        new Currency("USD", "United States Dollar"),
                        new Currency("UAH", "Ukrainian hryvna"),
                        BigDecimal.valueOf(26.3)));
    }
}

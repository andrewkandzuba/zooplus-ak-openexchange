package com.zooplus.openexchange.stubs;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.integrations.services.CurrenciesService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.zooplus.openexchange.integrations.gateways.CurrenciesGateway.*;

@Component
public class CurrenciesPlayServiceStub implements CurrenciesService {
    @Override
    @ServiceActivator(inputChannel = CURRENCIES_LIST_INBOUND_CHANNEL)
    public List<Currency> getCurrencies() {
        return Collections.singletonList(new Currency("USD", "United States Dollar"));
    }

    @Override
    @ServiceActivator(inputChannel = CURRENCIES_RATE_INBOUND_CHANNEL)
    public List<Rate> getRates(@Payload Date date, @Header(RATES_BASIC_CURRENCY_HEADER) Optional<Currency> basic) {
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

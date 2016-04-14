package com.zooplus.openexchange.stubs;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.integrations.services.CurrenciesService;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        return Collections.singletonList(new Rate(
                new Currency("USD", "United States Dollar"),
                new Currency("EUR", "European euro"),
                BigDecimal.valueOf(1.222)
        ));
    }
}

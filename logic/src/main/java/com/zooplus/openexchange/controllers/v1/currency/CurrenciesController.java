package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.protocol.ws.v1.Currencylistrequest;
import com.zooplus.openexchange.protocol.ws.v1.Currencylistresponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

import static com.zooplus.openexchange.controllers.v1.Version.CURRENCIES_LIST_API;
import static com.zooplus.openexchange.controllers.v1.Version.CURRENCIES_LIST_TOPIC;

@Controller
public class CurrenciesController {
    @Autowired
    CurrenciesGateway gateway;

    @MessageMapping(CURRENCIES_LIST_API)
    @SendTo(CURRENCIES_LIST_TOPIC)
    public Currencylistresponse list(Currencylistrequest request) throws Exception {
        List<Currency> currencies = gateway.getCurrencies();
        Currencylistresponse response = new Currencylistresponse();
        response.setCurrencies(currencies);
        return response;
    }
}

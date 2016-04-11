package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.protocol.ws.v1.Currencylistrequest;
import com.zooplus.openexchange.protocol.ws.v1.Currencylistresponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Controller
@RequestMapping(API_PATH_V1 + REST_ENDPOINT + CURRENCIES_ENDPOINT)
public class CurrenciesController {
    @Autowired
    CurrenciesGateway gateway;

    @RequestMapping(path = CURRENCIES_LIST_API, method = RequestMethod.GET)
    public Currencylistresponse list(Currencylistrequest request) throws Exception {
        List<Currency> currencies = gateway.getCurrencies();
        Currencylistresponse response = new Currencylistresponse();
        response.setCurrencies(currencies);
        return response;
    }
}

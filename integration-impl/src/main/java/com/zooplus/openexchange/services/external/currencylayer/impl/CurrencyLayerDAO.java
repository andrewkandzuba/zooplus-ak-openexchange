package com.zooplus.openexchange.services.external.currencylayer.impl;

import com.zooplus.openexchange.clients.RestClient;
import com.zooplus.openexchange.protocol.integration.Currencies;
import com.zooplus.openexchange.protocol.integration.Quotes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static com.zooplus.openexchange.services.cache.CacheConfiguration.*;
import static com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApiConstants.*;

@Component
public class CurrencyLayerDAO {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyLayerDAO.class.getName());
    @Value("${currencyplayer.api.accesskey}")
    private String accessKey;
    private RestClient restClient;

    @Cacheable(value = CURRENCIES_CACHE_NAME, keyGenerator = CUSTOM_CACHE_KEY_GENERATOR_NAME)
    public Currencies getCurrenciesListCached(int top) {
        logger.info("Retrieves value from an external service");
        ResponseEntity<Currencies> response = restClient.exchange(
                LIST_METHOD + "?"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1&top=" + top,
                HttpMethod.GET,
                RestClient.build(),
                Currencies.class);
        return response.getBody();
    }


    @Cacheable(value = QUOTES_CACHE_NAME, keyGenerator = CUSTOM_CACHE_KEY_GENERATOR_NAME)
    public Quotes getHistoricalQuotes(String currencyCode, String exchangeDate) {
        ResponseEntity<Quotes> response = restClient.exchange(
                String.format(HISTORICAL_METHOD + "?"
                        + CURRENCIES_PARAM + "=%s" + "&"
                        + DATE_PARAM + "=%s" + "&"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1", currencyCode, exchangeDate),
                HttpMethod.GET,
                RestClient.build(),
                Quotes.class);
        return response.getBody();
    }

    @PostConstruct
    private void init() {
        restClient = new RestClient(BASE_URL, 80);
    }
}

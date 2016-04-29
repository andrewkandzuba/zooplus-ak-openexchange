package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.protocol.integration.v1.Quotes;
import com.zooplus.openexchange.protocol.ws.v1.Currency;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.ExchangeRate;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@MessageEndpoint
public class CurrencyLayerResponseHandler {

    public static final String OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST = "out.success.pubsub.currencylayer.list";
    public static final String OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES = "out.success.pubsub.currencylayer.historicalquotes";

    @ServiceActivator(inputChannel = OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST)
    public CurrencyListResponse getCurrencyList(Message<Currencies> msg) {
        List<Currency> list = msg.getPayload()
                .getCurrencies()
                .entrySet()
                .stream()
                .map(entry -> {
                    Currency currency = new Currency();
                    currency.setCode(entry.getKey());
                    currency.setDescription(entry.getValue());
                    return currency;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(list);
        return response;
    }

    @ServiceActivator(inputChannel = OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesResponse getHistoricalQuotes(Message<Quotes> msg) {
        Quotes quotes = msg.getPayload();
        quotes.getSource();
        List<ExchangeRate> list = quotes.getQuotes()
                .entrySet()
                .stream()
                .map(entry -> {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setFrom(quotes.getSource());
                    exchangeRate.setTo(entry.getKey());
                    exchangeRate.setRate(entry.getValue());
                    return exchangeRate;
                })
                .collect(Collectors.toCollection(LinkedList::new));
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setExchangeDate(quotes.getDate());
        response.setExchangeRates(list);
        return response;
    }
}

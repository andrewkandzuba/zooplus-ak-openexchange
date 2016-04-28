package com.zooplus.openexchange.integrations.endpoints;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.database.domain.Rate;
import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.protocol.integration.v1.Quotes;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
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
                .map(entry -> new Currency(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        CurrencyListResponse response = new CurrencyListResponse();
        response.setCurrencies(list);
        return response;
    }

    @ServiceActivator(inputChannel = OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES)
    public HistoricalQuotesResponse getHistoricalQuotes(Message<Quotes> msg) {
        Quotes quotes = msg.getPayload();
        quotes.getSource();
        List<Rate> list = quotes.getQuotes()
                .entrySet()
                .stream()
                .map(entry -> new Rate(
                        new Currency(quotes.getSource(), ""),
                        new Currency(entry.getKey(), ""),
                        entry.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
        response.setRates(list);
        return response;
    }
}

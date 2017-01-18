package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.protocol.integration.CurrencyListRequest;
import com.zooplus.openexchange.protocol.integration.CurrencyListResponse;
import com.zooplus.openexchange.protocol.integration.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.integration.HistoricalQuotesResponse;
import com.zooplus.openexchange.services.external.currencylayer.api.CurrencyLayerApi;
import com.zooplus.openexchange.services.security.SecurityTokenValidator;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.zooplus.openexchange.protocol.integration.MetaInfo.CURRENCIES_RESOURCE;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    private CurrencyLayerApi currencyLayerApiGateway;
    @Autowired
    private SecurityTokenValidator securityTokenValidator;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                new SecurityJettyWebSocketHandler(securityTokenValidator,
                        (session, message, payloadClass) -> {
                            AtomicBoolean handled = new AtomicBoolean(false);
                            if (payloadClass.equals(CurrencyListRequest.class)) {
                                CurrencyListRequest request = (CurrencyListRequest) message;
                                currencyLayerApiGateway.getCurrenciesList(request.getTop())
                                        .addCallback(
                                                currencies -> {
                                                    try {
                                                        CurrencyListResponse response = new CurrencyListResponse();
                                                        response.setCurrencies(currencies);
                                                        session.sendMessage(MessageConvetor.to(response, CurrencyListResponse.class));
                                                        handled.compareAndSet(false, true);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                },
                                                Throwable::printStackTrace);
                            }
                            return handled.get();
                        },
                        (session, message, payloadClass) -> {
                            AtomicBoolean handled = new AtomicBoolean(false);
                            if (payloadClass.equals(HistoricalQuotesRequest.class)) {
                                HistoricalQuotesRequest request = (HistoricalQuotesRequest) message;
                                if (request.getCurrencyCode() == null) {
                                    throw new Exception("HistoricalQuotesRequest: currency code is empty!");
                                }
                                currencyLayerApiGateway.getHistoricalQuotes(request.getCurrencyCode(), request.getExchangeDate())
                                        .addCallback(
                                                quotes -> {
                                                    try {
                                                        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
                                                        response.setQuotes(quotes);
                                                        session.sendMessage(MessageConvetor.to(response, HistoricalQuotesResponse.class));
                                                        handled.compareAndSet(false, true);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                },
                                                Throwable::printStackTrace);
                            }
                            return handled.get();
                        }), CURRENCIES_RESOURCE)
                .setHandshakeHandler(defaultHandshakeHandler)
                .withSockJS();
    }
}
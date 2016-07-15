package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.SecurityJettyWebSocketHandler;
import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApi;
import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    private CurrencyLayerApi currencyLayerApiGateway;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                new SecurityJettyWebSocketHandler(
                        (session, message, payloadClass) -> {
                            AtomicBoolean handled = new AtomicBoolean(false);
                            if (payloadClass.equals(CurrencyListRequest.class)) {
                                CurrencyListRequest request = (CurrencyListRequest) message;
                                currencyLayerApiGateway.getCurrenciesList(request)
                                        .addCallback(
                                                currencies -> {
                                                    try {
                                                        session.sendMessage(MessageConvetor.to(currencies, CurrencyListResponse.class));
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
                                //@ToDo: The next line to be changed to getting real currency from cache
                                if (request.getCurrencyCode() == null) {
                                    throw new Exception("HistoricalQuotesRequest: currency code is empty!");
                                }
                                currencyLayerApiGateway.getHistoricalQuotes(request)
                                        .addCallback(
                                                rates -> {
                                                    try {
                                                        session.sendMessage(MessageConvetor.to(rates, HistoricalQuotesResponse.class));
                                                        handled.compareAndSet(false, true);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                },
                                                Throwable::printStackTrace);
                            }
                            return handled.get();
                        }), API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT)
                .setHandshakeHandler(defaultHandshakeHandler)
                .withSockJS();
    }
}
package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.integrations.gateways.CurrencyLayerApiGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    private CurrencyLayerApiGateway currencyLayerApiGateway;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                new JettyWebSocketHandler(
                        new MessageProcessor() {
                            @Override
                            public boolean supports(Class<?> payloadClass) {
                                return payloadClass.equals(CurrencyListRequest.class);
                            }

                            @Override
                            public void handle(WebSocketSession session, Object message) throws Exception {
                                CurrencyListRequest request = (CurrencyListRequest) message;
                                currencyLayerApiGateway.getCurrenciesList(request)
                                        .addCallback(
                                                currencies -> {
                                                    try {
                                                        session.sendMessage(MessageConvetor.to(currencies, CurrencyListResponse.class));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                },
                                                Throwable::printStackTrace);
                            }
                        },
                        new MessageProcessor() {
                            @Override
                            public boolean supports(Class<?> payloadClass) {
                                return payloadClass.equals(HistoricalQuotesRequest.class);
                            }

                            @Override
                            public void handle(WebSocketSession session, Object message) throws Exception {
                                HistoricalQuotesRequest request = (HistoricalQuotesRequest) message;
                                //@ToDo: The next line to be changed to getting real currency from cache
                                if(request.getCurrencyCode() == null){
                                   throw new Exception("HistoricalQuotesRequest: currency code is empty!");
                                }
                                currencyLayerApiGateway.getHistoricalQuotes(request)
                                        .addCallback(
                                                rates -> {
                                                    try {
                                                        session.sendMessage(MessageConvetor.to(rates, HistoricalQuotesResponse.class));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                },
                                                Throwable::printStackTrace);
                            }
                        }
                ), API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT)
                .setHandshakeHandler(defaultHandshakeHandler)
                .withSockJS();
    }
}
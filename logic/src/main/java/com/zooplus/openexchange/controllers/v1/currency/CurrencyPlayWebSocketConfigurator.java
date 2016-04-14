package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.integrations.gateways.CurrenciesGateway;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrenciesListResponse;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesRequest;
import com.zooplus.openexchange.protocol.ws.v1.HistoricalQuotesResponse;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.CURRENCIES_WS_ENDPOINT;
import static com.zooplus.openexchange.controllers.v1.Version.WS_ENDPOINT;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    CurrenciesGateway currenciesGateway;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                new JettyWebSocketHandler(
                        new MessageProcessor() {
                            @Override
                            public boolean supports(Class<?> payloadClass) {
                                return payloadClass.equals(CurrenciesListRequest.class);
                            }

                            @Override
                            public void handle(WebSocketSession session, Object message) throws Exception {
                                CurrenciesListResponse response = new CurrenciesListResponse();
                                response.setCurrencies(currenciesGateway.getCurrenciesList());
                                session.sendMessage(MessageConvetor.to(response, CurrenciesListResponse.class));
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
                                Currency basicCurrency = new Currency(Optional.ofNullable(request.getCurrencyCode()).orElse("USD"), "");
                                HistoricalQuotesResponse response = new HistoricalQuotesResponse();
                                response.setRates(currenciesGateway.getRates(Date.from(Instant.now()), Optional.of(basicCurrency)));
                                session.sendMessage(MessageConvetor.to(response, HistoricalQuotesResponse.class));
                            }
                        }
                ), API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT)
                .setHandshakeHandler(defaultHandshakeHandler)
                .withSockJS();
    }
}
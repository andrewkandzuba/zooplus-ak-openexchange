package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import com.zooplus.openexchange.controllers.MessageProcessor;
import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.integrations.gateways.CurrencyListGateway;
import com.zooplus.openexchange.integrations.gateways.CurrencyRatesGateway;
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

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    private CurrencyListGateway currencyListGateway;
    @Autowired
    private CurrencyRatesGateway currencyRatesGateway;

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
                                currencyListGateway.getCurrenciesList()
                                        .addCallback(
                                                currencies -> {
                                                    try {
                                                        CurrenciesListResponse response = new CurrenciesListResponse();
                                                        response.setCurrencies(currencies);
                                                        session.sendMessage(MessageConvetor.to(response, CurrenciesListResponse.class));
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
                                Currency basicCurrency = new Currency(request.getCurrencyCode(), "");
                                currencyRatesGateway.getRates(basicCurrency)
                                        .addCallback(
                                                rates -> {
                                                    try {
                                                        HistoricalQuotesResponse response = new HistoricalQuotesResponse();
                                                        response.setRates(rates);
                                                        session.sendMessage(MessageConvetor.to(response, HistoricalQuotesResponse.class));
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
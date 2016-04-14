package com.zooplus.openexchange.controllers.v1.currency;

import com.zooplus.openexchange.configurations.JettyWebSocketConfigurator;
import com.zooplus.openexchange.controllers.JettyWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.CURRENCIES_WS_ENDPOINT;
import static com.zooplus.openexchange.controllers.v1.Version.WS_ENDPOINT;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurator extends JettyWebSocketConfigurator {
    @Autowired
    private DefaultHandshakeHandler defaultHandshakeHandler;
    @Autowired
    private CurrenciesListRequestMessagesProcessor currenciesListRequestMessagesProcessor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new JettyWebSocketHandler(currenciesListRequestMessagesProcessor), API_PATH_V1 + WS_ENDPOINT + CURRENCIES_WS_ENDPOINT)
                .setHandshakeHandler(defaultHandshakeHandler)
                .withSockJS();
    }
}
package com.zooplus.openexchange.configuration;

import com.zooplus.openexchange.controllers.v1.currency.CentralEndpointHandler;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import static com.zooplus.openexchange.controllers.v1.Version.*;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurer implements WebSocketConfigurer {
    @Autowired
    private CentralEndpointHandler wsCurrenciesHandlers;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsCurrenciesHandlers, API_PATH_V1 + WS_ENDPOINT)
                .setHandshakeHandler(handshakeHandler())
                .withSockJS();
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        WebSocketPolicy policy = new WebSocketPolicy(WebSocketBehavior.SERVER);
        policy.setInputBufferSize(8192);
        policy.setIdleTimeout(600000);
        return new DefaultHandshakeHandler(
                new JettyRequestUpgradeStrategy(new WebSocketServerFactory(policy)));
    }
}
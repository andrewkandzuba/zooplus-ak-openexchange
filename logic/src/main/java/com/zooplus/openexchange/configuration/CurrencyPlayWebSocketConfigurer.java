package com.zooplus.openexchange.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;

@Configuration
@EnableWebSocket
public class CurrencyPlayWebSocketConfigurer implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), API_PATH_V1).withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new TextWebSocketHandler();
    }
}
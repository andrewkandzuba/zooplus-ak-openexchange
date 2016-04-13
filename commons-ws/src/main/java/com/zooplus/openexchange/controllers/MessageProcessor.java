package com.zooplus.openexchange.controllers;

import org.springframework.web.socket.WebSocketSession;

public interface MessageProcessor {
    boolean supports(Class<?> payloadClass);
    void handle(WebSocketSession session, Object message) throws Exception;
}

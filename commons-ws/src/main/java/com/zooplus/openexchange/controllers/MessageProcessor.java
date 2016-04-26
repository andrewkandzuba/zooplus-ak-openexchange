package com.zooplus.openexchange.controllers;

import org.springframework.web.socket.WebSocketSession;

public interface MessageProcessor {
    boolean onMessage(WebSocketSession session, Object message, Class<?> payloadClass) throws Exception;
}

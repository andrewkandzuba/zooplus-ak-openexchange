package com.zooplus.openexchange.controllers;

import com.zooplus.openexchange.protocol.ws.v1.MessageWrapper;
import com.zooplus.openexchange.utils.MessageConvetor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.Optional;

public class JettyWebSocketHandler extends TextWebSocketHandler {
    private final MessageProcessor[] processors;

    public JettyWebSocketHandler(MessageProcessor... processors) {
        this.processors = processors;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        MessageWrapper wrapper = MessageConvetor.from(message);
        Class clazz = Class.forName(wrapper.getPayloadClassName());
        Optional<MessageProcessor> processor = Arrays.stream(processors).filter(p -> p.supports(clazz)).findFirst();
        if (processor.isPresent()) {
            processor.get().handle(session, MessageConvetor.from(wrapper.getPayloadContent(), clazz));
        } else {
            throw new IllegalStateException("Unexpected WebSocket message type: " + clazz);
        }
    }
}

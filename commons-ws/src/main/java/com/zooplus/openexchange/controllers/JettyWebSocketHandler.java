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
        // Deserialize messageWrapper from payload
        MessageWrapper wrapper = MessageConvetor.unwrap(message.getPayload());
        // Get payload class
        Class clazz = Class.forName(wrapper.getPayloadClassName());
        // Try to find the first processor, which supports the payload
        Optional<MessageProcessor> processor = Arrays.stream(processors).filter(p -> p.supports(clazz)).findFirst();
        // Process the message, if processor is found
        if(processor.isPresent()){
            processor.get().handle(session, MessageConvetor.from(message.getPayload(), clazz));
        }
        // Throw exception otherwise
        throw new Exception(String.format("Message class %s is not supported", clazz.getName()));
    }
}

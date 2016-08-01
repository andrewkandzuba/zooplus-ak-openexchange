package com.zooplus.openexchange.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zooplus.openexchange.protocol.ws.MessageWrapper;
import org.springframework.web.socket.TextMessage;

public abstract class MessageConvetor {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static <P> TextMessage to(P payload, Class<P> clazz) throws Exception {
        MessageWrapper mw = new MessageWrapper();
        mw.setPayloadClassName(clazz.getName());
        mw.setPayloadContent(objectMapper.writerFor(clazz).writeValueAsString(payload));
        return new TextMessage(objectMapper.writerFor(MessageWrapper.class).writeValueAsString(mw));
    }

    public static MessageWrapper from(TextMessage message) throws Exception {
        return from(message.getPayload(), MessageWrapper.class);
    }

    public static <P> P from(String buffer, Class<P> clazz) throws Exception {
        return objectMapper.readValue(buffer, clazz);
    }
}

package com.zooplus.openexchange.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zooplus.openexchange.protocol.ws.v1.MessageWrapper;

import java.io.IOException;

public abstract class MessageConvetor {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static <K> String to(K payload, Class<K> clazz) throws JsonProcessingException {
        MessageWrapper mw = new MessageWrapper();
        mw.setPayloadClassName(clazz.getName());
        mw.setPayloadContent(objectMapper.writerFor(clazz).writeValueAsString(payload));
        return objectMapper.writerFor(MessageWrapper.class).writeValueAsString(mw);
    }
    public static Object from(String payload) throws IOException, ClassNotFoundException {
        MessageWrapper messageWrapper = objectMapper.readValue(payload, MessageWrapper.class);
        Class clazz = Class.forName(messageWrapper.getPayloadClassName());
        return objectMapper.readValue(messageWrapper.getPayloadContent(), clazz);
    }
    public static MessageWrapper unwrap(String payload) throws IOException, ClassNotFoundException {
        return objectMapper.readValue(payload, MessageWrapper.class);
    }
    public static <K> K from(String payload, Class<K> clazz) throws IOException {
       return clazz.cast(objectMapper.readValue(payload, clazz));
    }
}

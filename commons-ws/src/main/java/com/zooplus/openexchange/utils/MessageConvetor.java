package com.zooplus.openexchange.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

public interface MessageConvetor {
    static <K> String to(K payload, Class<K> clazz) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerFor(clazz);
        String encodedPayload = writer.writeValueAsString(payload);
        MessageWrapper mw = new MessageWrapper(clazz.getName(), encodedPayload);
        return mapper.writerFor(MessageWrapper.class).writeValueAsString(mw);
    }

    static Object from(String payload) throws IOException, ClassNotFoundException {
        MessageWrapper messageWrapper = new ObjectMapper().readValue(payload, MessageWrapper.class);
        Class clazz = Class.forName(messageWrapper.getPayloadClassName());
        return new ObjectMapper().readValue(messageWrapper.getPayloadContent(), clazz);
    }
}

package com.zooplus.openexchange.utils;

public class MessageWrapper {
    private String payloadClassName;
    private String payloadContent;

    public MessageWrapper() {
    }

    public MessageWrapper(String payloadClassName, String payloadContent) {
        this.payloadClassName = payloadClassName;
        this.payloadContent = payloadContent;
    }

    public String getPayloadClassName() {
        return payloadClassName;
    }

    public String getPayloadContent() {
        return payloadContent;
    }
}

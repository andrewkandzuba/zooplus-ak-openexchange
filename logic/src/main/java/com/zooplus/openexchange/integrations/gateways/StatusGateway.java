package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.protocol.v1.Status;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;

@MessagingGateway
public interface StatusGateway {
    String STATUS_GATEWAY_CHANNEL = "in.status.channel";
    @Gateway(requestChannel = STATUS_GATEWAY_CHANNEL)
    @Payload("new java.util.Date()")
    Status getStatus();
}

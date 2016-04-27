package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;


@MessagingGateway
public interface BorderConditionsGateway {
    String IN_DIRECT_CURRENCYLAYER = "in.direct.currencylayer";
    @Gateway(requestChannel = IN_DIRECT_CURRENCYLAYER)
    ListenableFuture<Void> throwNullPointerException(@Payload NullPointerExceptionMessage message);
}

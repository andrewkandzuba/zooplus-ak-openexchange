package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;


@MessagingGateway
public interface BorderConditionsGateway {
    String IN_DIRECT_CURRENCYLAYER_EXCEPTIONS = "in.direct.currencylayer.exceptions";
    @Gateway(requestChannel = IN_DIRECT_CURRENCYLAYER_EXCEPTIONS)
    ListenableFuture<Void> throwNullPointerException(@Payload NullPointerExceptionMessage message);

    String IN_DIRECT_CURRENCYLAYER_CACHE = "in.direct.currencylayer.cache";
    @Gateway(requestChannel = IN_DIRECT_CURRENCYLAYER_CACHE)
    @Cacheable(IN_DIRECT_CURRENCYLAYER_CACHE)
    ListenableFuture<String> cachableInvocations(@Payload String message);
}

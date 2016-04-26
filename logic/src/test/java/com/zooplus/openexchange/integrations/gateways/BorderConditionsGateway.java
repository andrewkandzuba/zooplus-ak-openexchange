package com.zooplus.openexchange.integrations.gateways;

import com.zooplus.openexchange.protocol.v1.NullPointerExceptionMessage;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFuture;

import static com.zooplus.openexchange.integrations.configurations.CurrencyLayerApiIntegrationConfiguration.IN_DIRECT_CURRENCYLAYER;

@MessagingGateway(
        defaultRequestChannel = IN_DIRECT_CURRENCYLAYER,
        defaultHeaders = @GatewayHeader(name = "calledMethod", expression = "#gatewayMethod.name")
)
public interface BorderConditionsGateway {
    ListenableFuture<Void> throwNullPointerException(@Payload NullPointerExceptionMessage message);
}

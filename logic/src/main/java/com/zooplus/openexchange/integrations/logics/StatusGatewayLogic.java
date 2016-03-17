package com.zooplus.openexchange.integrations.logics;

import com.zooplus.openexchange.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import static com.zooplus.openexchange.integrations.gateways.StatusGateway.STATUS_GATEWAY_CHANNEL;

@MessageEndpoint
public class StatusGatewayLogic {
    @Autowired
    StatusService service;

    @Bean
    public IntegrationFlow httpFlow() {
        return IntegrationFlows.from(STATUS_GATEWAY_CHANNEL)
                .handle(service)
                .get();
    }
}

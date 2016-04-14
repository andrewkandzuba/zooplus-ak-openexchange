package com.zooplus.openexchange.integrations.logics;

import com.zooplus.openexchange.integrations.services.CurrenciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;

import static com.zooplus.openexchange.integrations.gateways.CurrenciesGateway.CURRENCIES_INBOUND_CHANNEL;

@MessageEndpoint
public class CurrencyGatewayLogic {
    @Autowired
    CurrenciesService service;

    @Bean
    public IntegrationFlow httpFlow() {
        return IntegrationFlows.from(CURRENCIES_INBOUND_CHANNEL)
                .handle(service)
                .get();
    }
}

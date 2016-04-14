package com.zooplus.openexchange.integrations.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static com.zooplus.openexchange.integrations.gateways.CurrenciesGateway.CURRENCIES_INBOUND_CHANNEL;

@Configuration
public class ChannelConfiguration {
    @Bean(name = CURRENCIES_INBOUND_CHANNEL)
    public MessageChannel CurrenciesRequestChannel() {
        return MessageChannels.direct().get();
    }
}

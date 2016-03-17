package com.zooplus.openexchange.integrations.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static com.zooplus.openexchange.integrations.gateways.StatusGateway.STATUS_GATEWAY_CHANNEL;

@Configuration
public class ChannelConfiguration {
    @Bean(name = STATUS_GATEWAY_CHANNEL)
    public MessageChannel cfStatusRequestChannel() {
        return MessageChannels.direct().get();
    }
}

package com.zooplus.openexchange.integrations.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan("com.zooplus.openexchange.integrations.gateways")
@EnableIntegration
public class CurrencyListIntegrationConfiguration {
    public static final String IN_DIRECT_CURRENCIES_LIST = "in.direct.currencies.list";
    public static final String IN_REST_CURRENCIES_LIST = "in.rest.currencies.list";
    public static final String OUT_PUBSUB_CURRENCIES_LIST = "out.pubsub.currencies.list";

    @Bean(name = IN_DIRECT_CURRENCIES_LIST)
    public MessageChannel requestChannel() {
        return MessageChannels.direct(IN_DIRECT_CURRENCIES_LIST).get();
    }

    @Bean(name = IN_REST_CURRENCIES_LIST)
    public MessageChannel invocationChannel() {
        return MessageChannels.direct(IN_REST_CURRENCIES_LIST).get();
    }

    @Bean(name = OUT_PUBSUB_CURRENCIES_LIST)
    public MessageChannel responseChannel() {
        return MessageChannels.publishSubscribe(OUT_PUBSUB_CURRENCIES_LIST).get();
    }
}

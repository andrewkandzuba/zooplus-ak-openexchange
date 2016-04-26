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
public class CurrencyLayerApiIntegrationConfiguration {
    public static final String IN_DIRECT_CURRENCYLAYER = "in.direct.currencylayer";
    public static final String IN_API_CURRENCYLAYER = "in.api.currencylayer";
    public static final String OUT_PUBSUB_CURRENCYLAYER = "out.pubsub.currencylayer";

    public static final String HEADER_HISTORICAL_QUOTES_REQUEST = "in.header.historical.quotes.request";
    public static final String HEADER_CURRENCY_LIST_REQUEST = "in.header.currency.list.request";

    @Bean(name = IN_DIRECT_CURRENCYLAYER)
    public MessageChannel requestChannel() {
        return MessageChannels.direct(IN_DIRECT_CURRENCYLAYER).get();
    }

    @Bean(name = IN_API_CURRENCYLAYER)
    public MessageChannel invocationChannel() {
        return MessageChannels.direct(IN_API_CURRENCYLAYER).get();
    }

    @Bean(name = OUT_PUBSUB_CURRENCYLAYER)
    public MessageChannel responseChannel() {
        return MessageChannels.publishSubscribe(OUT_PUBSUB_CURRENCYLAYER).get();
    }
}

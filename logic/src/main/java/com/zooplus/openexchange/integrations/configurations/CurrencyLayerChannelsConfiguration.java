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
public class CurrencyLayerChannelsConfiguration {
    public static final String IN_DIRECT_CURRENCYLAYER_LIST = "in.direct.currencylayer.list";
    public static final String IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES = "in.direct.currencylayer.historicalquotes";

    public static final String IN_API_CURRENCYLAYER_LIST = "in.api.currencylayer.list";
    public static final String IN_API_CURRENCYLAYER_HISTORICALQUOTES = "in.api.currencylayer.historicalquotes";

    public static final String OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST = "out.success.pubsub.currencylayer.list";
    public static final String OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES = "out.success.pubsub.currencylayer.historicalquotes";

    @Bean(name = IN_DIRECT_CURRENCYLAYER_LIST)
    public MessageChannel directListChannel() {
        return MessageChannels.direct(IN_DIRECT_CURRENCYLAYER_LIST).get();
    }

    @Bean(name = IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES)
    public MessageChannel directHistoricalQuotesChannel() {
        return MessageChannels.direct(IN_DIRECT_CURRENCYLAYER_HISTORICALQUOTES).get();
    }

    @Bean(name = IN_API_CURRENCYLAYER_LIST)
    public MessageChannel apiListChannel() {
        return MessageChannels.direct(IN_API_CURRENCYLAYER_LIST).get();
    }

    @Bean(name = IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public MessageChannel apiHistoricalQuotesChannel() {
        return MessageChannels.direct(IN_API_CURRENCYLAYER_HISTORICALQUOTES).get();
    }

    @Bean(name = OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST)
    public MessageChannel pubSubListResponseChannel() {
        return MessageChannels.publishSubscribe(OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST).get();
    }

    @Bean(name = OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES)
    public MessageChannel pubSubHistoricalQuotesResponseChannel() {
        return MessageChannels.publishSubscribe(OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES).get();
    }
}

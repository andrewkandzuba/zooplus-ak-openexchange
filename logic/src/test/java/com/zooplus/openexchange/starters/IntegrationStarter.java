package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

import static com.zooplus.openexchange.integrations.api.CurrencyPlayerIntegrationFlow.INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;
import static com.zooplus.openexchange.integrations.api.CurrencyPlayerIntegrationFlow.OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations.gateway",
                "com.zooplus.openexchange.integrations.services",
                "com.zooplus.openexchange.integrations.api",
                "com.zooplus.openexchange.controllers.v1"
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class IntegrationStarter {
    @Bean(name = INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    MessageChannel httpInboundChannel() {
        return MessageChannels.direct(INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST).get();
    }

    @Bean(name = OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    MessageChannel httpOutboundChannel() {
        return MessageChannels.direct(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST).get();
    }
}

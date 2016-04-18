package com.zooplus.openexchange.integrations.stubs;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;

//@Configuration
//
@Component
@EnableIntegration
public class CurrencyPlayerIntegrationFlow {
    public static final String INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST = "in.channel.api.currencyplayer.list";
    public static final String OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST = "out.channel.api.currencyplayer.list";

    @Bean(name = OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    MessageChannel httpOutboundChannel() {
        return MessageChannels.direct(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST).get();
    }

    @Bean
    @ServiceActivator(inputChannel = INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    public HttpRequestExecutingMessageHandler httpRequestExecutingMessageHandler() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(
                URI.create("http://www.apilayer.net/api/list?access_key=aaf13c95d3b6669707e8d2af72ac82b5&format=1"));
        handler.setHttpMethod(HttpMethod.GET);
        handler.setExpectedResponseType(Currencies.class);
        handler.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        handler.setCharset("UTF-8");
        handler.setOutputChannelName(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST);
        return handler;
    }

    /*@Bean
    IntegrationFlow flow(){
        return f -> f.channel(INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST).handle(httpRequestExecutingMessageHandler());
    }*/
}

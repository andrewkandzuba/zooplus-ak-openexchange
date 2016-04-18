package com.zooplus.openexchange.integrations.api;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;

@Component
@EnableIntegration
public class CurrencyPlayerIntegrationFlow {
    private static final String BASE_URL = "http://apilayer.net/api/";
    private static final String ENDPOINT_LIST = "list";
    @Value("${currencyplayer.api.accesskey}")
    private String ACCESS_KEY;

    public static final String INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST = "in.channel.api.currencyplayer.list";
    public static final String OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST = "out.channel.api.currencyplayer.list";

    @Bean
    @ServiceActivator(inputChannel = INBOUND_CHANNEL_API_CURRENCYPLAYER_LIST)
    public HttpRequestExecutingMessageHandler httpRequestExecutingMessageHandler() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(
                URI.create(BASE_URL + ENDPOINT_LIST + "?access_key=" + ACCESS_KEY + "&format=1"));
        handler.setHttpMethod(HttpMethod.GET);
        handler.setExpectedResponseType(Currencies.class);
        handler.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        handler.setCharset("UTF-8");
        handler.setOutputChannelName(OUTBOUND_CHANNEL_API_CURRENCYPLAYER_LIST);
        return handler;
    }
}

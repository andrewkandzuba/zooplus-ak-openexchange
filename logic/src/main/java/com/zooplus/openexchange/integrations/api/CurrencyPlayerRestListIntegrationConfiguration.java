package com.zooplus.openexchange.integrations.api;

import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;

import java.net.URI;
import java.util.Collections;

import static com.zooplus.openexchange.integrations.api.CurrencyPlayerRestConstants.BASE_URL;
import static com.zooplus.openexchange.integrations.api.CurrencyPlayerRestConstants.ENDPOINT_LIST;
import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.IN_REST_CURRENCIES_LIST;
import static com.zooplus.openexchange.integrations.configurations.CurrencyListIntegrationConfiguration.OUT_PUBSUB_CURRENCIES_LIST;

@Configuration
public class CurrencyPlayerRestListIntegrationConfiguration {
    @Value("${currencyplayer.api.accesskey}")
    private String ACCESS_KEY;

    @Bean(name = "restListHandler")
    @ServiceActivator(inputChannel = IN_REST_CURRENCIES_LIST)
    public HttpRequestExecutingMessageHandler httpRequestExecutingMessageHandler() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(
                URI.create(BASE_URL + ENDPOINT_LIST + "?access_key=" + ACCESS_KEY + "&format=1"));
        handler.setHttpMethod(HttpMethod.GET);
        handler.setExpectedResponseType(Currencies.class);
        handler.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        handler.setCharset("UTF-8");
        handler.setOutputChannelName(OUT_PUBSUB_CURRENCIES_LIST);
        return handler;
    }
}

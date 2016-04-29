package com.zooplus.openexchange.integrations.api;

import com.zooplus.openexchange.integrations.endpoints.CurrencyLayerRequestBuilder;
import com.zooplus.openexchange.integrations.endpoints.CurrencyLayerResponseHandler;
import com.zooplus.openexchange.protocol.integration.v1.Currencies;
import com.zooplus.openexchange.protocol.integration.v1.Quotes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.zooplus.openexchange.integrations.api.CurrencyLayerApiConstants.*;

@Configuration
public class CurrencyLayerApiIntegrationConfiguration {
    @Value("${currencyplayer.api.accesskey}")
    private String accessKey;

    @Bean(name = "restCurrencyLayerListHandler")
    @ServiceActivator(inputChannel = CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_LIST)
    public HttpRequestExecutingMessageHandler restCurrencyLayerListHandler() {
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(
                BASE_URL + LIST_METHOD + "?"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1");
        handler.setHttpMethod(HttpMethod.GET);
        handler.setExpectedResponseType(Currencies.class);
        handler.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        handler.setCharset("UTF-8");
        handler.setOutputChannelName(CurrencyLayerResponseHandler.OUT_SUCCESS_PUBSUB_CURRENCYLAYER_LIST);
        return handler;
    }

    @Bean(name = "restCurrencyLayerHistoricalQuotesListHandler")
    @ServiceActivator(inputChannel = CurrencyLayerRequestBuilder.IN_API_CURRENCYLAYER_HISTORICALQUOTES)
    public HttpRequestExecutingMessageHandler restCurrencyLayerHistoricalQuotesListHandler() {
        SpelExpressionParser expressionParser = new SpelExpressionParser();
        Map<String, Expression> uriVariableExpressions = new HashMap<>(2);
        uriVariableExpressions.put("currencies", expressionParser.parseExpression("payload.currencyCode"));
        uriVariableExpressions.put("exchangeDate", expressionParser.parseExpression("payload.exchangeDate"));
        HttpRequestExecutingMessageHandler handler = new HttpRequestExecutingMessageHandler(
                BASE_URL + HISTORICAL_METHOD + "?"
                        + CURRENCIES_PARAM + "={currencies}" + "&"
                        + DATE_PARAM + "={exchangeDate}" + "&"
                        + ACCESS_KEY_PARAM + "=" + accessKey + "&"
                        + FORMAT_PARAM + "=1");
        handler.setHttpMethod(HttpMethod.GET);
        handler.setUriVariableExpressions(uriVariableExpressions);
        handler.setExpectedResponseType(Quotes.class);
        handler.setMessageConverters(Collections.singletonList(new MappingJackson2HttpMessageConverter()));
        handler.setCharset("UTF-8");
        handler.setOutputChannelName(CurrencyLayerResponseHandler.OUT_SUCCESS_PUBSUB_CURRENCYLAYER_HISTORICALQUOTES);
        return handler;
    }
}

package com.zooplus.openexchange.integrations.configurations;

import com.zooplus.openexchange.protocol.ws.v1.Currency;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListRequest;
import com.zooplus.openexchange.protocol.ws.v1.CurrencyListResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.support.GenericHandler;

import java.util.Collections;
import java.util.Map;

@Configuration
@EnableAutoConfiguration
@IntegrationComponentScan
public class IntegrationFlowConfiguration {
    public static final String UPCASE_INPUT = "upcase.input";

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext ctx =
                SpringApplication.run(IntegrationFlowConfiguration.class, args);
        CurrencyListResponse response = ctx.getBean(Upcase.class).upcase(new CurrencyListRequest());
        System.out.println(response.getCurrencies().get(0).getCode());
        ctx.close();
    }

    @MessagingGateway
    public interface Upcase {
        @Gateway(requestChannel = UPCASE_INPUT)
        CurrencyListResponse upcase(CurrencyListRequest request);
    }

    @Bean
    public IntegrationFlow flow() {
        return f -> f
                .channel(UPCASE_INPUT)
                .<CurrencyListRequest, CurrencyListResponse>transform(o -> {
                    CurrencyListResponse response = new CurrencyListResponse();
                    Currency currency = new Currency();
                    currency.setCode("USD");
                    currency.setDescription("United States Dollar");
                    response.setCurrencies(Collections.singletonList(currency));
                    return response;
                })
                .handle(new GenericHandler<CurrencyListResponse>() {
                    @Override
                    public Object handle(CurrencyListResponse currencyListResponse, Map<String, Object> map) {
                        return currencyListResponse;
                    }
                });
    }
}

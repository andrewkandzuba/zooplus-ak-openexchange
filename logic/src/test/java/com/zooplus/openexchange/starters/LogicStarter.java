package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.database.domain.Currency;
import com.zooplus.openexchange.services.CurrenciesService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;

import java.util.Collections;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.integrations"
        })
@IntegrationComponentScan("com.zooplus.openexchange.integrations")
public class LogicStarter {
    @Bean
    CurrenciesService getCurrenciesService() {
        return () -> Collections.singletonList(new Currency("USD", "United States Dollar"));
    }
}

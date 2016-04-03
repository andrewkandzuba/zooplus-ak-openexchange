package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.database",
                "com.zooplus.openexchange.service.utils"
        })
@PropertySource("classpath:config/environment-test.properties")
public class RepositoriesStarter {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
}

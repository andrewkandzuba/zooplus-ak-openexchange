package com.zooplus.openexchange.starters;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.database",
                "com.zooplus.openexchange.utils"
        })
@PropertySource("classpath:config/environment-test.properties")
public class RepositoriesStarter {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
}

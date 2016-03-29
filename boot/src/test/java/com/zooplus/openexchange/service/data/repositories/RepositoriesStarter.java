package com.zooplus.openexchange.service.data.repositories;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.data",
                "com.zooplus.openexchange.service.security",
                "com.zooplus.openexchange.service.utils"
        })
@PropertySource("classpath:config/environment-test.properties")
@Profile("development")
class RepositoriesStarter {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
}

package com.zooplus.openexchange.service.data.repositories;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.data",
                "com.zooplus.openexchange.service.security2"
        })
@Profile("development")
class RepositoriesStarter {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
}

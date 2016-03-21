package com.zooplus.openexchange.service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@PropertySource("classpath:config/environment-development.properties")
@Profile("development")
public class DevelopmentApplicationConfiguration {
    @Bean
    public ExecutorService executorService(){
       return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
}

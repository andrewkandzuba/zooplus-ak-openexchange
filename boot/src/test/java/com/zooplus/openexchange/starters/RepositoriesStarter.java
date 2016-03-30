package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.service.security.cache.AuthenticationService;
import com.zooplus.openexchange.service.security.cache.TokenService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.database",
                "com.zooplus.openexchange.service.security",
                "com.zooplus.openexchange.service.utils"
        })
@PropertySource("classpath:config/environment-test.properties")
@Profile("development")
public class RepositoriesStarter {
    @Bean
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }
    @Bean
    public AuthenticationService authenticationService() {
        return Mockito.mock(AuthenticationService.class);
    }
    @Bean
    public TokenService tokenService(){
        return Mockito.mock(TokenService.class);
    }
}

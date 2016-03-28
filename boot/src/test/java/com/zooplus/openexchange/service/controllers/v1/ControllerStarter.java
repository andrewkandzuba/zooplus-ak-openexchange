package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.controllers",
                "com.zooplus.openexchange.service.security2"
        })
@PropertySource("classpath:config/environment-development.properties")
@Profile("test")
public class ControllerStarter {
    @Bean(name = "userRepository")
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}

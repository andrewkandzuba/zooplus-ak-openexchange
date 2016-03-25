package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = {"com.zooplus.openexchange.service.controllers.v1.subscription"})
@Profile("development")
public class RegistrationControllerStater {
    @Bean
    public UserRepository getSubscriberRepository(){
        return Mockito.mock(UserRepository.class);
    }
}

package com.zooplus.openexchange.service.controllers.v1.registration;

import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.controllers.v1.registration",
                "com.zooplus.openexchange.service.security"
        })
@Profile("development")
class RegistrationControllerStater {
    @Bean
    public UserRepository getSubscriberRepository(){
        return Mockito.mock(UserRepository.class);
    }
}

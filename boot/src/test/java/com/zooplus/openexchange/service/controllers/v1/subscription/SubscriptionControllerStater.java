package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = {"com.zooplus.openexchange.service.controllers.v1.subscription"})
@Profile("development")
public class SubscriptionControllerStater {
    @Bean
    public SubscriberRepository getSubscriberRepository(){
        return Mockito.mock(SubscriberRepository.class);
    }
}

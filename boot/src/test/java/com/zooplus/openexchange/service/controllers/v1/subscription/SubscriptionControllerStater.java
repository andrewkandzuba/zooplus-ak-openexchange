package com.zooplus.openexchange.service.controllers.v1.subscription;

import com.zooplus.openexchange.service.data.domain.Subscriber;
import com.zooplus.openexchange.service.data.repositories.SubscriberRepository;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication(scanBasePackages = {"com.zooplus.openexchange.service.controllers.v1.subscription"})
@Profile("development")
public class SubscriptionControllerStater {
    @Mock
    public SubscriberRepository subscriberRepository;

    @Bean
    SubscriberRepository getSubscriberRepository(){
        Subscriber s = new Subscriber();
        s.setId(1L);
        s.setEmail("RS@AK.COM");
        s.setPassword("1234");
        MockitoAnnotations.initMocks(this);
        Mockito.when(subscriberRepository.findByEmail(Mockito.anyString())).thenReturn(null);
        Mockito.when(subscriberRepository.saveAndFlush(Mockito.any(Subscriber.class))).thenReturn(s);
        return subscriberRepository;
    }

}

package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.service.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.controllers.v1",
                "com.zooplus.openexchange.service.utils",
                "com.zooplus.openexchange.service.security"
        })
@PropertySource("classpath:config/environment-test.properties")
public class ControllersStarter {
    @Bean
    public RoleRepository roleRepository(){
        return Mockito.mock(RoleRepository.class);
    }
    @Bean
    public UserRepository userRepository(){
        return Mockito.mock(UserRepository.class);
    }
}

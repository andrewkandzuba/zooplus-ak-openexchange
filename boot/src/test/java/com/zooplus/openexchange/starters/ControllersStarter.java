package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.service.frontend.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.frontend.database.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.frontend.controllers.v1",
                "com.zooplus.openexchange.service.frontend.utils",
                "com.zooplus.openexchange.service.frontend.security"
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

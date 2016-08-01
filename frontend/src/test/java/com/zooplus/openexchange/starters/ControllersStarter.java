package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers",
                "com.zooplus.openexchange.utils",
                "com.zooplus.openexchange.security.filters",
                "com.zooplus.openexchange.security.providers",
                "com.zooplus.openexchange.security.configurations.stubs"
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

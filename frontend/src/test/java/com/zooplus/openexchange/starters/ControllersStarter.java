package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.database.repositories.RoleRepository;
import com.zooplus.openexchange.database.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.controllers.v1",
                "com.zooplus.openexchange.utils",
                "com.zooplus.openexchange.security"
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
    @Bean
    public CsrfTokenRepository csrfTokenRepository(){
        return Mockito.mock(CsrfTokenRepository.class);
    }
}

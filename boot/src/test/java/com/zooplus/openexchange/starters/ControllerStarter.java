package com.zooplus.openexchange.starters;

import com.zooplus.openexchange.service.database.repositories.RoleRepository;
import com.zooplus.openexchange.service.database.repositories.UserRepository;
import com.zooplus.openexchange.service.security.cache.AuthenticationService;
import com.zooplus.openexchange.service.security.cache.TokenService;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.controllers",
                "com.zooplus.openexchange.service.security",
                "com.zooplus.openexchange.service.utils",
                "com.zooplus.openexchange.service.redis"
        })
@PropertySource("classpath:config/environment-test.properties")
@Profile("test")
public class ControllerStarter {
    @Bean(name = "userRepository")
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
    @Bean(name = "roleRepository")
    public RoleRepository roleRepository() {
        return Mockito.mock(RoleRepository.class);
    }
    @Bean
    public AuthenticationService authenticationRepository() {
        return Mockito.mock(AuthenticationService.class);
    }
    @Bean
    public TokenService tokenService(){
        return Mockito.mock(TokenService.class);
    }
}
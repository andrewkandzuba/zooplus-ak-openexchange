package com.zooplus.openexchange.service.controllers.v1;

import com.zooplus.openexchange.service.data.cache.AuthenticationService;
import com.zooplus.openexchange.service.data.cache.TokenService;
import com.zooplus.openexchange.service.data.repositories.RoleRepository;
import com.zooplus.openexchange.service.data.repositories.UserRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(
        scanBasePackages = {
                "com.zooplus.openexchange.service.controllers",
                "com.zooplus.openexchange.service.security",
                "com.zooplus.openexchange.service.utils"
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

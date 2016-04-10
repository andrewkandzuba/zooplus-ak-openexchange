package com.zooplus.openexchange.security.configurations.stubs;

import com.zooplus.openexchange.security.configurations.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@Configuration
@Profile("controllers")
public class SecurityConfigurationStub extends SecurityConfiguration {

    @Override
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        return new CsrfTokenRepositoryStub();
    }

}

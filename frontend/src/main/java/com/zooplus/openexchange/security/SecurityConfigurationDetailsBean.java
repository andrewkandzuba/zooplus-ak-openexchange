package com.zooplus.openexchange.security;

import com.zooplus.openexchange.security.providers.DataSourceAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

import static com.zooplus.openexchange.controllers.v1.Version.ADMIN_ENDPOINT;
import static com.zooplus.openexchange.controllers.v1.Version.USER_LOGIN_PATH;
import static com.zooplus.openexchange.controllers.v1.Version.USER_REGISTRATION_PATH;

@Configuration
public class SecurityConfigurationDetailsBean implements SecurityConfigurationDetails {
    @Override
    public String[] actuatorEndpoints() {
        return new String[]{ADMIN_ENDPOINT, USER_REGISTRATION_PATH};
    }

    @Override
    public String[] permitAllEndpoints() {
        return new String[]{USER_LOGIN_PATH};
    }

    @Override
    public String authenticationPath() {
        return USER_LOGIN_PATH;
    }

    @Override
    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new DataSourceAuthenticationProvider();
    }
}

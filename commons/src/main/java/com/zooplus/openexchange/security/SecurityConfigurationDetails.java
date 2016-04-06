package com.zooplus.openexchange.security;

import org.springframework.security.authentication.AuthenticationProvider;

public interface SecurityConfigurationDetails {
    String X_AUTH_USERNAME_HEADER = "x-auth-username";
    String X_AUTH_PASSWORD_HEADER = "x-auth-password";
    String X_AUTH_TOKEN_HEADER = "x-auth-token";

    String[] actuatorEndpoints();
    String[] permitAllEndpoints();
    String authenticationPath();
    AuthenticationProvider authenticationProvider();

}

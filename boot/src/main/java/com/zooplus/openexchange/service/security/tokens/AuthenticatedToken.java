package com.zooplus.openexchange.service.security.tokens;

import org.springframework.security.core.Authentication;

public class AuthenticatedToken {
    private String token;
    private Authentication authentication;

    public AuthenticatedToken(String token, Authentication authentication) {
        this.token = token;
        this.authentication = authentication;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Authentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}

package com.zooplus.openexchange.service.security.tokens;


import org.springframework.security.core.Authentication;

public interface AuthenticatedTokenRepository {
    AuthenticatedToken findByAuthentication(Authentication authentication);
}

package com.zooplus.openexchange.service.security.tokens;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticatedTokenRepositoryImpl implements AuthenticatedTokenRepository {
    @Override
    @Cacheable(cacheNames = TokenCacheConfiguration.TOKENS_CACHE_NAME)
    public AuthenticatedToken findByAuthentication(Authentication authentication) {
        return new AuthenticatedToken(generateNewToken(), authentication);
    }

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }
}

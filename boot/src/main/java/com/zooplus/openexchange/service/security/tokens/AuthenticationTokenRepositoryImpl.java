package com.zooplus.openexchange.service.security.tokens;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationTokenRepositoryImpl implements AuthenticationTokenRepository{
    private final Cache<String, Authentication> cache;

    public AuthenticationTokenRepositoryImpl() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }


    @Override
    public void store(String token, Authentication authentication) {
        cache.put(token, authentication);
    }

    @Override
    public Authentication findByToken(String token) {
        return cache.getIfPresent(token);
    }
}

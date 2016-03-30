package com.zooplus.openexchange.service.data.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Cache<String, Authentication> cache;

    public AuthenticationServiceImpl() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public void cache(Authentication authentication) {
        cache.put(authentication.getDetails().toString(), authentication);
    }

    @Override
    public void evict(Authentication authentication) {
        cache.invalidate(authentication.getDetails().toString());
    }

    @Override
    public Authentication get(String token) {
        return cache.getIfPresent(token);
    }
}

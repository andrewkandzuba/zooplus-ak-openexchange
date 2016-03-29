package com.zooplus.openexchange.service.security.tokens;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.guava.GuavaCacheManager;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class TokenCacheConfiguration {
    public static final String TOKENS_CACHE_NAME = "tokens_cache";
    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager(TOKENS_CACHE_NAME);
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(10, TimeUnit.MINUTES);
        cacheManager.setCacheBuilder(cacheBuilder);
        return cacheManager;
    }
}
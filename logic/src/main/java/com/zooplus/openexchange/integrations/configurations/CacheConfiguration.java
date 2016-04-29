package com.zooplus.openexchange.integrations.configurations;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String CACHE_CURRENCYLAYER_LIST = "cache.currencylayer.list";
    public static final String CACHE_CURRENCYLAYER_HISTORICALQUOTES = "cache.currencylayer.historicalquotes";
    @Bean
    public CacheManager getEhCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}

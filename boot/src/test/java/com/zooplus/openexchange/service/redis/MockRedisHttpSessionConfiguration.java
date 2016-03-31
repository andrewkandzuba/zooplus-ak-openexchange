package com.zooplus.openexchange.service.redis;

import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

/*@Configuration
@EnableRedisHttpSession*/
public class MockRedisHttpSessionConfiguration extends RedisHttpSessionConfiguration {
    @Bean
    @Override
    public RedisOperationsSessionRepository sessionRepository(RedisOperations<Object, Object> sessionRedisTemplate, ApplicationEventPublisher applicationEventPublisher) {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(
                sessionRedisTemplate);
        sessionRepository.setDefaultMaxInactiveInterval(100);
        return sessionRepository;
    }
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisConnectionFactory factory = Mockito.mock(RedisConnectionFactory.class);
        RedisConnection connection = Mockito.mock(RedisConnection.class);
        Mockito.when(factory.getConnection()).thenReturn(connection);
        return factory;
    }
}
package com.zooplus.openexchange.security.configurations.stubs;

import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;

@Configuration
@EnableRedisHttpSession
@Profile("controllers")
public class RedisHttpSessionConfigurationStub extends RedisHttpSessionConfiguration {
    @Bean
    @Override
    public RedisOperationsSessionRepository sessionRepository(RedisOperations<Object, Object> sessionRedisTemplate, ApplicationEventPublisher applicationEventPublisher) {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(
                redisOperations());
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
    @Bean
    public RedisOperations<Object,Object> redisOperations(){
        return Mockito.mock(RedisOperations.class);
    }
    @Bean
    public BoundValueOperations<Object, Object> boundValueOperations(){
        return Mockito.mock(BoundValueOperations.class);
    }
    @Bean
    public BoundHashOperations<Object, Object, Object> boundHashOperations(){
        return Mockito.mock(BoundHashOperations.class);
    }
    @Bean
    public BoundSetOperations<Object, Object> boundSetOperations(){
        return Mockito.mock(BoundSetOperations.class);
    }
}
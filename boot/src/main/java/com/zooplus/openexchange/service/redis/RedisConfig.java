package com.zooplus.openexchange.service.redis;

import org.springframework.cloud.Cloud;
import org.springframework.cloud.CloudFactory;
import org.springframework.cloud.service.common.RedisServiceInfo;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/*@Configuration
@Profile("cloud")*/
public class RedisConfig {
    //@Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        CloudFactory cloudFactory = new CloudFactory();
        Cloud cloud = cloudFactory.getCloud();
        RedisServiceInfo serviceInfo = (RedisServiceInfo) cloud.getServiceInfo("my-redis");
        return cloud.getServiceConnector(serviceInfo.getId(), JedisConnectionFactory.class, null);
    }
    //@Bean
    public RedisTemplate<String, String> redisTemplate() {
        return new StringRedisTemplate(jedisConnectionFactory());
    }
}

package com.example.cache.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * spring boot 配置多数据源redis配置类
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis-02.host}")
    private String host;
    @Value("${spring.redis-02.port}")
    private String port;
    @Value("${spring.redis-02.password}")
    private String password;
    @Value("${spring.redis-02.database}")
    private String database;

    @Value("${spring.redis-02.jedis.pool.max-idle}")
    private String maxIdle;
    @Value("${spring.redis-02.jedis.pool.min-idle}")
    private String minIdle;
    @Value("${spring.redis-02.jedis.pool.max-active}")
    private String maxActive;
    @Value("${spring.redis-02.jedis.pool.max-wait}")
    private String maxWait;

    @Bean(name = "stringRedisTemplateTest")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        RedisConnectionFactory redisConnectionFactory = connectionFactory();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        //创建json序列化对象
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer =
                new GenericJackson2JsonRedisSerializer();
        //设置value的序列化方式
        stringRedisTemplate.setValueSerializer(new StringRedisSerializer());
        //设置key序列化方式string
        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

    /**
     * redis标准配置
     *
     * @return
     */
    private RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(Integer.parseInt(database));
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(Integer.parseInt(port));
        redisStandaloneConfiguration.setPassword(password);
        return redisStandaloneConfiguration;
    }

    private RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = redisStandaloneConfiguration();
        GenericObjectPoolConfig genericObjectPoolConfig = genericObjectPoolConfig();
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder lettuceClientConfigurationBuilder =
                LettucePoolingClientConfiguration.builder();
        lettuceClientConfigurationBuilder.poolConfig(genericObjectPoolConfig);
        //lettuceClientConfigurationBuilder.commandTimeout();
        LettuceConnectionFactory lettuce = new LettuceConnectionFactory(redisStandaloneConfiguration,
                lettuceClientConfigurationBuilder.build());
        lettuce.afterPropertiesSet();//连接池初始化
        return lettuce;
    }

    /**
     * redis连接池通用配置
     *
     * @return
     */
    private GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(Integer.parseInt(maxActive));
        genericObjectPoolConfig.setMaxWaitMillis(Integer.parseInt(maxWait));
        genericObjectPoolConfig.setMaxIdle(Integer.parseInt(maxIdle));
        genericObjectPoolConfig.setMinIdle(Integer.parseInt(minIdle));
        return genericObjectPoolConfig;
    }
}

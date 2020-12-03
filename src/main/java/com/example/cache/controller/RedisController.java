package com.example.cache.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Controller
public class RedisController {
    @Resource(name = "stringRedisTemplateTest")
    private StringRedisTemplate stringRedisTemplate;

    public void setValue(String key, String value, Long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
}

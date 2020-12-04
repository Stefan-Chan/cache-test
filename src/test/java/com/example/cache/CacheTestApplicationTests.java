package com.example.cache;

import com.example.cache.controller.RedisController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CacheTestApplicationTests {

    @Autowired
    private RedisController redisController;

    @Test
    void contextLoads() {
    }

    @Test
    void redisTest() {
        redisController.setValue("testKey", "testValue", 10L);
        String value = redisController.getValue("testKey");
        System.out.println("the redis value is :" + value);
    }
}

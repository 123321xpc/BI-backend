package com.vickey.bi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedissonLimiterTest {

    @Resource
    private RedissonLimiter redissonLimiter;

    @Test
    void doLimit() {
        for(int i=0;i<10;i++){
            redissonLimiter.doLimit("test");
            System.out.println("成功");
        }

    }
}
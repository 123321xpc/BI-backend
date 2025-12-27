package com.vickey.bi.manager;

import com.vickey.bi.common.ErrorCode;
import com.vickey.bi.exception.ThrowUtils;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Component
public class RedissonLimiter {

    @Resource
    private RedissonClient redissonClient;


    public void doLimit(String key){
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        //  每一秒钟限流5次
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        boolean ok = rateLimiter.tryAcquire(1);

        if(!ok){
            ThrowUtils.throwIf(true, ErrorCode.TOO_MANY_REQUESTS_ERROR);
        }
    }
}

package com.yn.printer.service.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis 分布式锁工具
 *
 * @author : Jonas Chan
 * @since : 2024/3/1 17:29
 */
@Component
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean lock(String key, String value) {

        Boolean lock = redisTemplate.opsForValue().setIfAbsent(key, value);
        redisTemplate.expire(key, 60, TimeUnit.SECONDS);

        return lock != null && lock;
    }

    public boolean unlock(String key) {

        Boolean lock = redisTemplate.delete(key);

        return lock != null && lock;
    }

}

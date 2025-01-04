package kz.nik.project01produserredis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void cacheObject(String key, Object value, long timeout, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public Object getCachedObject(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteCachedObject(String key){
        redisTemplate.delete(key);
    }
}

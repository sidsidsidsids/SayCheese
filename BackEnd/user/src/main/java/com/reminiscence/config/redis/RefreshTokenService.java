package com.reminiscence.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh-token:";

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String userId, String refreshToken, long expirationSeconds) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken);
        redisTemplate.expire(key, expirationSeconds, TimeUnit.SECONDS);
    }

    public String getRefreshToken(String userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteRefreshToken(String userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}

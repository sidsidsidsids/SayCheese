package com.reminiscence.config.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenRevocationServiceImpl implements TokenRevocationService{

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenRevocationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void revokeAccessToken(String userId, String accessToken) {
        String key = RedisKey.REVOKED_TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, accessToken);
        redisTemplate.expire(key, RedisKey.REVOKED_TOKEN_EXPIRATION_TIME, TimeUnit.SECONDS);
    }

    @Override
    public String getRevokedToken(String userId) {
        String key = RedisKey.REVOKED_TOKEN_KEY_PREFIX + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

}

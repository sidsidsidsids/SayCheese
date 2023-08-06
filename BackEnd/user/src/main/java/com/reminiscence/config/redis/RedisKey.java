package com.reminiscence.config.redis;

public interface RedisKey {
    String REFRESH_TOKEN_KEY_PREFIX = "Bearer ";
    String REVOKED_TOKEN_KEY_PREFIX = "Invalid ";
    int REVOKED_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000 ; // 60분;
}

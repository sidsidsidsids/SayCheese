package com.reminiscence.config.redis;

public interface RedisKey {
    String REFRESH_TOKEN_KEY_PREFIX = "Bearer ";
    String REVOKED_TOKEN_KEY_PREFIX = "Invalid ";

    String EMAIL_AUTH_TOKEN_PREFIX="Email-Auth-Token: ";
    int REVOKED_TOKEN_EXPIRATION_TIME = 60 * 60 * 1000 ; // 60분;

    int EMAIL_AUTH_TOKEN_EXPIRATION_TIME=5*60*1000;
}

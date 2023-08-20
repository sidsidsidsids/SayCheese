package com.reminiscence.filter;

public interface JwtProperties {

//    864000000 -> 10일 (1/1000초)
    int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분
    int REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000 * 14; // 14일
    int GUEST_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000 * 1; // 1일
    String TOKEN_PREFIX = "Bearer ";
    String REFRESH_TOKEN_HEADER = "RefreshToken";
//    String ACCESS_TOKEN_PREFIX = "accessToken:";
//    String REFRESH_TOKEN_PREFIX = "refreshToken:";

    String HEADER_STRING = "Authorization";
}
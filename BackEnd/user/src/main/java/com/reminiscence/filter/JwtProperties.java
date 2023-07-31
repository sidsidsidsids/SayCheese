package com.reminiscence.filter;

public interface JwtProperties {
    String SECRET = "청바지"; // 우리 서버만 알고 있는 비밀값
//    864000000 -> 10일 (1/1000초)
    int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분
    int REFRESH_TOKEN_EXPIRATION_TIME = 60 * 60 * 24 * 1000 * 14; // 14일
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
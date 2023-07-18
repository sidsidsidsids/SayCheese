package com.reminiscence;

import java.util.Map;

public interface JwtService {

	<T> String createAccessToken(String key, T data);
	<T> String createRefreshToken(String key, T data);
//	<T> String create(String key, T data, String subject);
	<T> String create(String key, T data, String subjectm, long expir);
	Map<String, Object> get(String key);
	String getUserId();
	boolean isUsable(String jwt);
	
}

package com.reminiscence.room.filter;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final String SECRET_KEY;

    @Autowired
    public JwtUtil(Environment env) {
        this.SECRET_KEY = env.getProperty("jwt.secret");
    }

    // JWT 토큰의 만료 시간 계산
    public Date generateExpirationDate(int expirationTime) {
        return new Date(System.currentTimeMillis() + expirationTime); // 계산된 만료 시간 반환
    }

    public String extractClaimValue(String token, String claimName) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            return String.valueOf(claims.get(claimName)); // 클레임 값 가져오기

        } catch (Exception e) {
            // 토큰 파싱이 실패한 경우 또는 클레임이 없는 경우 예외 처리
            e.printStackTrace();
            return null;
        }
    }

    // Request의 Header에서 AccessToken 값을 가져옵니다. "Authorization" : "Bearer "
    public String resolveAccessToken(HttpServletRequest request) {
        String requestHeader = request.getHeader(JwtProperties.HEADER_STRING);

        if(requestHeader != null && requestHeader.startsWith(JwtProperties.TOKEN_PREFIX))
            return requestHeader.substring(JwtProperties.TOKEN_PREFIX.length());
        return null;
    }

    public boolean isTokenExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String generateToken(String username, int expiration_time, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(expiration_time))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public void addHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    }

    public Map<String, Object> setCustomClaims(Map<String, Object> customClaims, String key, String value) {
        customClaims.put(key, value);

        return customClaims;
    }






}

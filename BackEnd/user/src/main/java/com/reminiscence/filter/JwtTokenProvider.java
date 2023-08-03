package com.reminiscence.filter;
import com.reminiscence.config.auth.MemberDetail;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

import static com.reminiscence.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final String SECRET_KEY;

    private final JwtUtil jwtUtil;

    @Autowired
    public JwtTokenProvider(Environment env, JwtUtil jwtUtil) {
        this.SECRET_KEY = env.getProperty("jwt.secret");
        this.jwtUtil = jwtUtil;
    }

    public String generateToken(String username, int expiration_time, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(jwtUtil.generateExpirationDate(expiration_time))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    // 토큰에서 회원 정보 추출 (이메일 정보)
    public String getUsernameFromToken(String token) {
        String exractedToken = token.substring(JwtProperties.TOKEN_PREFIX.length()).trim();
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(exractedToken)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token, MemberDetail memberDetail) {
        String username = getUsernameFromToken(token);
        return username.equals(memberDetail.getUsername()) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return false;
        }
    }

    // refreshToken을 쿠키에서 추출할 경우
//    public String getRefreshTokenFromCookie(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("refreshToken".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }

    // refreshToken을 쿠키에 담을 경우
//    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
//        Cookie cookie = new Cookie("refreshToken", refreshToken);
//        cookie.setHttpOnly(true); // JavaScript로 쿠키에 접근 불가능하도록 설정
//        cookie.setSecure(true);   // HTTPS 프로토콜을 통해 전송할 때만 쿠키 사용
//        cookie.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME); // 쿠키의 유효 기간 설정 (초 단위)
//        cookie.setPath("/");     // 쿠키의 경로 설정 (애플리케이션의 모든 경로에서 접근 가능하도록 설정)
//        response.addCookie(cookie);
//    }

    // 어세스 토큰 헤더 설정
    public void addHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
    }

    // 리프레시 토큰 헤더 설정
    // 사용자로부터 헤더 값으로 리프레시 토큰을 받는 것을 테스트하는 용도로, 실제 구현에서는 쿠키 값으로 전달하므로 빼야 함
    public void addHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.addHeader(JwtProperties.REFRESH_TOKEN_HEADER, JwtProperties.TOKEN_PREFIX + refreshToken);
    }
}

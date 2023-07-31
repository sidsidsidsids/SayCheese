package com.reminiscence.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reminiscence.config.redis.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.member.dto.MemberLoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import static com.reminiscence.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.reminiscence.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    private final Environment env;

    private final RefreshTokenService refreshTokenService;

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String refreshToken = getRefreshTokenFromCookie(request);
        if (refreshToken != null) {
            String userId = extractClaimValue(refreshToken, "memberId", env);
            if (refreshToken.equals(refreshTokenService.getRefreshToken(userId))) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
                return authenticationManager.authenticate(authentication);
            }
        }

        // request에 있는 email과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        MemberLoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), MemberLoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter : " + loginRequestDto);

        // 이메일패스워드 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(),
                loginRequestDto.getPassword());
        System.out.println("JwtAuthenticationFilter : 토큰생성완료");

        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
        Authentication authentication =
                authenticationManager.authenticate(token);
        MemberDetail memberDetail = (MemberDetail) authentication.getPrincipal();
        if (memberDetail.getMember() != null)
            System.out.println("Authentication : " + memberDetail.getMember().getEmail());
        return authentication;
    }

    // JWT Token 생성해서 response에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        MemberDetail memberDetail = (MemberDetail) authResult.getPrincipal();

        String accessToken = JWT.create()
                .withSubject(memberDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
//                .withClaim("id", memberDetail.getMember().getId())
                .withClaim("memberId", String.valueOf(memberDetail.getMember().getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));

        String refreshToken = JWT.create()
                .withSubject(memberDetail.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
//                .withClaim("id", memberDetail.getMember().getId())
                .withClaim("memberId", String.valueOf(memberDetail.getMember().getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        System.out.println(refreshToken);
        System.out.println(REFRESH_TOKEN_EXPIRATION_TIME);
        System.out.println(memberDetail.getMember().getId());
        refreshTokenService.saveRefreshToken(String.valueOf(memberDetail.getMember().getId()), refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);
        addRefreshTokenToCookie(response, refreshToken);

    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true); // JavaScript로 쿠키에 접근 불가능하도록 설정
        cookie.setSecure(true);   // HTTPS 프로토콜을 통해 전송할 때만 쿠키 사용
        cookie.setMaxAge(REFRESH_TOKEN_EXPIRATION_TIME); // 쿠키의 유효 기간 설정 (초 단위)
        cookie.setPath("/");     // 쿠키의 경로 설정 (애플리케이션의 모든 경로에서 접근 가능하도록 설정)
        response.addCookie(cookie);
    }

    public static String extractClaimValue(String token, String claimName, Environment env) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(env.getProperty("jwt.secret"))
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            return claims.get(claimName, String.class); // 클레임 값 가져오기

        } catch (Exception e) {
            // 토큰 파싱이 실패한 경우 또는 클레임이 없는 경우 예외 처리
            e.printStackTrace();
            return null;
        }
    }
}
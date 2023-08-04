package com.reminiscence.handler;

import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.filter.JwtProperties;
import com.reminiscence.filter.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private RefreshTokenService refreshTokenService;
    private JwtUtil jwtUtil;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader(JwtProperties.HEADER_STRING, ""); // 테스트 용 (헤더 내 Accesss 토큰 제거) -> 추후 redis 설정 완료 시 변경 필요

//        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
//        String refreshToken = request.getHeader(JwtProperties.REFRESH_TOKEN_HEADER);
//        refreshTokenService.deleteRefreshToken(refreshToken);

        // accessToken BlackList 설정

    }
}

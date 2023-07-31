package com.reminiscence.handler;

import com.reminiscence.filter.JwtProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setHeader(JwtProperties.HEADER_STRING, ""); // 테스트 용 (헤더 내 Accesss 토큰 제거) -> 추후 redis 설정 완료 시 변경 필요
    }
}

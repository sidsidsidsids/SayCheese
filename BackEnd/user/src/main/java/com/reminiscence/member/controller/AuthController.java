package com.reminiscence.member.controller;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.filter.JwtTokenProvider;
import com.reminiscence.filter.JwtUtil;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.AuthResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.reminiscence.filter.JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.reminiscence.filter.JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/api/refresh")
    public ResponseEntity<?> refreshToken(@AuthenticationPrincipal MemberDetail memberDetail, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpStatus status = HttpStatus.ACCEPTED;
        String refreshToken = jwtUtil.resolveToken(request);
        Object message = null;
        log.debug("token : {}, memberDetail : {}", refreshToken, memberDetail);
        if (refreshToken.equals(refreshTokenService.getRefreshToken(String.valueOf(memberDetail.getMember().getId())))) {
            Map<String, Object> customClaims = new HashMap<>();
            customClaims.put("memberId", String.valueOf(memberDetail.getMember().getId()));
            String newAccessToken = jwtTokenProvider.generateToken(memberDetail.getUsername(), ACCESS_TOKEN_EXPIRATION_TIME, customClaims);
            String newRefreshToken = jwtTokenProvider.generateToken(memberDetail.getUsername(), REFRESH_TOKEN_EXPIRATION_TIME, customClaims);
            jwtTokenProvider.addHeaderAccessToken(response, newAccessToken);
            jwtTokenProvider.addHeaderRefreshToken(response, newRefreshToken);
            log.debug("token : {}", newAccessToken);
            log.debug("정상적으로 액세스토큰 재발급!!!");
            status = HttpStatus.OK;
            message = Response.of(AuthResponseMessage.ACCESS_TOKEN_REISSUE_SUCCESS);
        } else {
            log.debug("리프레쉬토큰도 사용불가!!!!!!!");
            status = HttpStatus.UNAUTHORIZED;
            message = Response.of(AuthResponseMessage.ACCESS_TOKEN_REISSUE_FAIL);
        }
        return new ResponseEntity<>(message, status);
    }

}

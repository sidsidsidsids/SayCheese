package com.reminiscence.member.controller;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.domain.Member;
import com.reminiscence.filter.JwtTokenProvider;
import com.reminiscence.filter.JwtUtil;
import com.reminiscence.member.service.MemberService;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.AuthResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static com.reminiscence.filter.JwtProperties.*;

@RestController
@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final RefreshTokenService refreshTokenService;

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtUtil jwtUtil;

    @PostMapping("/refresh")
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
            jwtTokenProvider.setHeaderAccessToken(response, newAccessToken);
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

    @GetMapping("/guest")
    public ResponseEntity generateGuestToken(@RequestParam(required = false, defaultValue = "") String nickname, HttpServletResponse response) throws Exception {
        Member guest = memberService.joinGuestMember(nickname);
        String username = guest.getEmail();
        Map<String, Object> customClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(guest.getId()));

        String guestToken = jwtTokenProvider.generateToken(username, GUEST_TOKEN_EXPIRATION_TIME, customClaims);
        jwtTokenProvider.setHeaderAccessToken(response, guestToken);

        return new ResponseEntity<>(Response.of(AuthResponseMessage.GUEST_TOKEN_ISSUE_SUCCESS), HttpStatus.OK);
    }

}

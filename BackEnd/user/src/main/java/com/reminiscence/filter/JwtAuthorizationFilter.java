package com.reminiscence.filter;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.domain.Member;
import com.reminiscence.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final Environment env;
    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = null;
        String refreshToken = null;
        // 토큰 유무 확인
        String header=request.getHeader(JWTKey.REQUIRED_HEADER);
        System.out.println(header);
        if(header==null || !header.startsWith(JWTKey.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        } else if (request.getHeader(JWTKey.REQUIRED_HEADER).startsWith(JWTKey.TOKEN_PREFIX+JwtProperties.ACCESS_TOKEN_PREFIX)) {
            accessToken = jwtUtil.resolveAccessToken(request);
        } else if (request.getHeader(JWTKey.REQUIRED_HEADER).startsWith(JWTKey.TOKEN_PREFIX+JwtProperties.REFRESH_TOKEN_PREFIX)) {
            refreshToken = jwtUtil.resolveRefreshToken(request);
        }
        System.out.println(accessToken);
        System.out.println(refreshToken);

//        String token=header.substring(JWTKey.TOKEN_PREFIX.length()).trim();
        String secretKey=env.getProperty("jwt.secret");
        logger.debug("secretKey: "+ secretKey);
        if(secretKey==null) {
            logger.error("JWT key is not exists!!");
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰에서 memberId 부분만 추출
        String memberId= jwtUtil.extractClaimValue(accessToken, "memberId");
//        String memberId= JWT.require(Algorithm.HMAC512(secretKey)).build()
//                .verify(token)
//                .getClaim("memberId")
//                .asString();
        // token 값을 권한 처리를 위해 Authentication에 주입
        if(memberId!=null){
            Member member=memberRepository.findById(Long.parseLong(memberId)).orElse(null);
            MemberDetail memberDetail = new MemberDetail(member);
            // accessToken이 검증될 경우
            if (!jwtTokenProvider.isTokenExpired(accessToken)){
                UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDetail,null, memberDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
            }
            // refreshTokenToken이 검증될 경우
            else if(refreshTokenService.getRefreshToken(memberId) != null && jwtTokenProvider.validateToken(refreshTokenService.getRefreshToken(memberId), memberDetail)){

                UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDetail,null, memberDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
            }

            UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDetail,null, memberDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
        }
////        String username = obtainUsername(request);
////        String password = obtainPassword(request);
//        String refreshToken = jwtUtil.getRefreshTokenFromCookie(request);
////        System.out.println(username);
////        System.out.println(username);
////        System.out.println(password);
////        System.out.println(password);
//        if (refreshToken != null) {
//            String userId = jwtUtil.extractClaimValue(refreshToken, "memberId", env);
////            System.out.println(username);
//            System.out.println(userId);
////            if(jwtUtil.validateToken(refreshTokenService.getRefreshToken()))
//            if (refreshToken.equals(refreshTokenService.getRefreshToken(userId))) {
////                Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
////                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }

        chain.doFilter(request,response);
    }

    private interface JWTKey{
        public static final String REQUIRED_HEADER="Authorization";
        public static final String TOKEN_PREFIX="Bearer ";
    }

}

package com.reminiscence.room.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.domain.Member;
import com.reminiscence.room.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
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
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final Environment env;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 유무 확인
        String token = jwtUtil.resolveAccessToken(request);


        if(token==null){
            chain.doFilter(request,response);
            return;
        }

        String secretKey=env.getProperty("jwt.secret");
        logger.debug("secretKey: "+ secretKey);
        if(secretKey==null) {
            logger.error("JWT key is not exists!!");
            chain.doFilter(request, response);
            return;
        }

        if (!jwtUtil.isTokenExpired(token)){
            String errorMessage = "Access 토큰이 만료되었습니다.";
            response.sendError(HttpStatus.UNAUTHORIZED.value(), errorMessage);
            return;
        }


        // JWT 토큰에서 memberId 부분만 추출
        String memberId= jwtUtil.extractClaimValue(token, "memberId");

        // token 값을 권한 처리를 위해 Authentication에 주입
        if(memberId!=null){
            Member member=memberRepository.findById(Long.parseLong(memberId)).orElse(null);
            UserDetail userDetail=new UserDetail(member);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userDetail,null,userDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }


        chain.doFilter(request,response);
    }


    private interface JWTKey{
        public static final String REQUIRED_HEADER="Authorization";
        public static final String TOKEN_PREFIX="Bearer ";

    }
}

package com.reminiscence.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.domain.Member;
import com.reminiscence.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
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
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 토큰 유무 확인
        String header=request.getHeader(JWTKey.REQUIRED_HEADER);
        if(header==null || !header.startsWith(JWTKey.TOKEN_PREFIX)){
            chain.doFilter(request,response);
            return;
        }

        String token=header.substring(JWTKey.TOKEN_PREFIX.length());
        String secretKey=env.getProperty("jwt.secret");
        logger.debug("secretKey: "+ secretKey);
        if(secretKey==null) {
            logger.error("JWT key is not exists!!");
            chain.doFilter(request, response);
            return;
        }

        // JWT 토큰에서 memberId 부분만 추출
        String memberId= JWT.require(Algorithm.HMAC512(secretKey)).build()
                .verify(token)
                .getClaim("memberId")
                .asString();

        // token 값을 권한 처리를 위해 Authentication에 주입
        if(memberId!=null){
            Member member=memberRepository.findById(Long.parseLong(memberId)).orElse(null);
            MemberDetail memberDetail =new MemberDetail(member);
            UsernamePasswordAuthenticationToken emailPasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(memberDetail.getMember().getEmail(),null, memberDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
        }

        chain.doFilter(request,response);
    }


    private interface JWTKey{
        public static final String REQUIRED_HEADER="Authorization";
        public static final String TOKEN_PREFIX="Bearer ";

    }
}

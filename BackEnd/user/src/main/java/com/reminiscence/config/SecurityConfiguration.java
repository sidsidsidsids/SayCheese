package com.reminiscence.config;

import com.nimbusds.oauth2.sdk.http.HTTPEndpoint;
import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.config.redis.TokenRevocationService;
import com.reminiscence.domain.Role;
import com.reminiscence.filter.JwtAuthenticationFilter;
import com.reminiscence.filter.JwtAuthorizationFilter;
import com.reminiscence.filter.JwtTokenProvider;
import com.reminiscence.filter.JwtUtil;
import com.reminiscence.handler.AccessDenyHandler;
import com.reminiscence.handler.CustomAuthenticationEntryPoint;
import com.reminiscence.handler.CustomLogoutSuccessHandler;
import com.reminiscence.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final Environment env;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CorsConfig corsConfig;

    @Autowired
    private TokenRevocationService tokenRevocationService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // 경로 접근 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .addFilter(corsConfig.corsFilter())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager, refreshTokenService, jwtTokenProvider, jwtUtil, "/api/login"))
                .addFilterBefore(new JwtAuthorizationFilter(env, memberRepository, refreshTokenService, jwtTokenProvider, jwtUtil), BasicAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET,"/api/auth/guest").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/member/info").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                    .antMatchers(HttpMethod.POST,"/api/member/id-check").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/member/nickname-check").permitAll()
                    .antMatchers(HttpMethod.POST,"/api/member/password").permitAll()
                    .antMatchers(HttpMethod.PUT,"/api/member/password").permitAll()
                    .antMatchers(HttpMethod.GET,"/api/member/nickname").authenticated()
                    .antMatchers(HttpMethod.PUT,"/api/member/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                    .antMatchers(HttpMethod.DELETE,"/api/member/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                    .antMatchers("/api/mail/auth").permitAll()
                .antMatchers("/api/mail/auth/check").permitAll()
                .anyRequest().permitAll()
                .and()
                .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler(refreshTokenService, jwtUtil, tokenRevocationService));

        http.exceptionHandling().accessDeniedHandler(new AccessDenyHandler());
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 접근 설정 예외 경로 설정
//    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/**.html", "/**.css", "/img/**");
    }
}

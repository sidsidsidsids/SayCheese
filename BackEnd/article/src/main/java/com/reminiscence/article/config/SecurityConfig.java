package com.reminiscence.article.config;

import com.reminiscence.article.domain.Role;
import com.reminiscence.article.filter.JWTAuthorizationFilter;
import com.reminiscence.article.filter.JwtUtil;
import com.reminiscence.article.handler.AccessDenyHandler;
import com.reminiscence.article.handler.AuthenticaitionEntryPoint;
import com.reminiscence.article.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final Environment env;
    private final MemberRepository memberRepository;
    private final CorsConfig corsConfig;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilter(corsConfig.corsFilter());
        http.addFilterBefore(new JWTAuthorizationFilter(env,memberRepository, jwtUtil), BasicAuthenticationFilter.class);
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/notice").permitAll()
                .and()
                .authorizeRequests().antMatchers("/actuator/**").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers("/api/article/notice").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/frame/my/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/frame/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST,"/api/frame/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST,"/api/article/frame/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/article/frame/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/notice/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT,"/api/article/notice/**").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/faq/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/image/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST,"/api/article/image/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/article/image/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers("/api/image/random/tag").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/image/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers("/api/article/lover/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers("/api/article/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().antMatchers("/docs/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/amazon/**").hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                .and()
                .authorizeRequests().anyRequest().authenticated();
        http.exceptionHandling().authenticationEntryPoint(new AuthenticaitionEntryPoint());
        http.exceptionHandling().accessDeniedHandler(new AccessDenyHandler());
        http.csrf().disable();
        http.httpBasic().disable();
        http.formLogin().disable();
        return http.build();
    }
}

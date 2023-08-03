package com.reminiscence.article.config;

import com.reminiscence.article.filter.JWTAuthorizationFilter;
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
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JWTAuthorizationFilter(env,memberRepository), BasicAuthenticationFilter.class);
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/notice").permitAll()
                .and()
                .authorizeRequests().antMatchers("/actuator/**").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers("/api/article/notice").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/article/notice/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT,"/api/article/notice/**").access("hasRole('ADMIN')")
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET,"/api/faq/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/image/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/article/image").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/article/**").authenticated()
                .and()
                .authorizeRequests().antMatchers("/docs/**").permitAll()
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

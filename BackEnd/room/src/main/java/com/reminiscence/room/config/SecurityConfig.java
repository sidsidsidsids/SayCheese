package com.reminiscence.room.config;


import com.reminiscence.room.domain.Role;
import com.reminiscence.room.filter.JWTAuthorizationFilter;
import com.reminiscence.room.filter.JwtUtil;
import com.reminiscence.room.handler.AccessDenyHandler;
import com.reminiscence.room.handler.AuthenticaitionEntryPoint;
import com.reminiscence.room.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilter(corsConfig.corsFilter());
        http.addFilterBefore(new JWTAuthorizationFilter(env,memberRepository, jwtUtil), BasicAuthenticationFilter.class);
        http.authorizeRequests().antMatchers("/api/room").permitAll()
                .and()
                .authorizeRequests().antMatchers("/api/room/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/docs/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.GET, "/api/participant/**").permitAll()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/api/participant/**").authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.PUT, "/api/participant/**").authenticated()
                .and()
                .authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/participant/**").authenticated()
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

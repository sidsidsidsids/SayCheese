package com.reminiscence.config;

import com.reminiscence.config.redis.RefreshTokenService;
import com.reminiscence.config.redis.TokenRevocationService;
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
//                .antMatchers("/public").permitAll()
//                .antMatchers("/private").hasRole("USER")
                    .antMatchers(HttpMethod.PUT,"/api/member/**").authenticated()
                    .antMatchers(HttpMethod.DELETE,"/api/member/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler(refreshTokenService, jwtUtil, tokenRevocationService))
                    .logoutSuccessUrl("/public");
//                .and().apply(new Custom())
//                .antMatchers("/api/article/image/**")
//                .access("hasRole('ROLE_Member') or hasRole('ROLE_ADMIN')")
//                .antMatchers("/api/admin/**")
//                .access("hasRole('ROLE_ADMIN')")
//                .anyRequest().permitAll();

        http.exceptionHandling().accessDeniedHandler(new AccessDenyHandler());
        http.exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        return http.build();
    }


//    public class Custom extends AbstractHttpConfigurer<Custom, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, env, refreshTokenService, jwtTokenProvider);
//            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
//            http
//                    .addFilter(jwtAuthenticationFilter);
//
//        }
//    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 접근 설정 예외 경로 설정
//    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/**.html", "/**.css", "/img/**");
    }

//    @Bean
//    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
//        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new JwtAuthenticationFilter(authenticationManager));
//        registrationBean.addUrlPatterns("api/member/login"); // Filter가 적용될 URL 패턴 설정
//        return registrationBean;
//    }
    // using the WebSecurityConfigurerAdapter with an embedded DataSource that is initialized with the default schema and has a single user
//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//                .build();
//    }
//
//    @Bean
//    public UserDetailsManager users(DataSource dataSource) {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//        users.createUser(user);
//        return users;
//    }
}

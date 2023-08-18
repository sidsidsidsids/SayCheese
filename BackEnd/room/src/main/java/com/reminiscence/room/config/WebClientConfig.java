package com.reminiscence.room.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Profile("!test")
public class WebClientConfig {

    @Value("${spring.data.openvidu-url}")
    private String BASE_URL;
    @Value("${spring.data.openvidu-secret}")
    private String SECRET;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, SECRET)
                .build();
    }
}

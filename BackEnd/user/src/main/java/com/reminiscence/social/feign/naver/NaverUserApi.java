package com.reminiscence.social.feign.naver;

import java.util.Map;

import com.reminiscence.social.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "naverUser", url="https://openapi.naver.com", configuration = {FeignConfiguration.class})
public interface NaverUserApi {
    @GetMapping("/v1/nid/me")
    ResponseEntity<String> getUserInfo(@RequestHeader Map<String, String> header);
}
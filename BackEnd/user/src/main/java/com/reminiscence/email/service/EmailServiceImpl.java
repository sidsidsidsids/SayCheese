package com.reminiscence.email.service;

import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final int EMAIL_TOKEN_LENGTH=4;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate redisTemplate;
    private final Environment env;
    private final String AUTH_TOKEN_SUBJECT="회원가입 이메일 인증 임시 메일입니다.";
    @Override
    public void storeAuthToken(EmailRequestDto emailRequestDto) {
        String auth=createAuthToken(emailRequestDto.getEmail());
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(emailRequestDto.getEmail());
        simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
        simpleMailMessage.setSubject(AUTH_TOKEN_SUBJECT);
        StringBuilder textBuilder=new StringBuilder();
        textBuilder.append("이메일 임시 인증 토큰은 ").append(auth).append("입니다.");
        simpleMailMessage.setText(textBuilder.toString());
        try{
            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            log.error("{} 쿼리 시도 중 발생 {},",emailRequestDto.getEmail(),e);

        }
    }

    private String createAuthToken(String email){
        String tempTokenValue= generateRandomNumbers(EMAIL_TOKEN_LENGTH);
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_TOKEN_PREFIX+email,tempTokenValue, Duration.ofMillis(RedisKey.EMAIL_AUTH_TOKEN_EXPIRATION_TIME));
        return tempTokenValue;
    }

    private String generateRandomNumbers(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder result = new StringBuilder();
        Random random = new Random();

        while (result.length() < length) {
            int digit = random.nextInt(10);
            result.append(digit);
        }

        return result.toString();
    }
}

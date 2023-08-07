package com.reminiscence.email.service;

import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate redisTemplate;
    private final Environment env;
    private final String AUTH_TOKEN_SUBJECT="회원가입 이메일 인증 임시 메일입니다.";
    @Override
    public void sendAuthToken(EmailRequestDto emailRequestDto) {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setTo(emailRequestDto.getEmail());
        simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
        simpleMailMessage.setSubject(AUTH_TOKEN_SUBJECT);
        simpleMailMessage.setText("안녕하세요");
        javaMailSender.send(simpleMailMessage);
    }
}

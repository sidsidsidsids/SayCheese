package com.reminiscence.email.service;

import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.email.EmailType;
import com.reminiscence.email.dto.EmailCheckRequestDto;
import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.exception.customexception.EmailException;
import com.reminiscence.exception.message.EmailExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final int EMAIL_TOKEN_LENGTH = 4;
    private final JavaMailSender javaMailSender;
    private final RedisTemplate redisTemplate;
    private final Environment env;
    private final String AUTH_TOKEN_SUBJECT = "회원가입 이메일 인증 임시 메일입니다.";
    private final String FIND_PASSWORD_SUBJECT = "[세이치즈] 비밀번호 변경 안내";

    @Override
    public void storeAuthToken(EmailRequestDto emailRequestDto, EmailType emailType) {
        String auth = createAuthToken(emailRequestDto.getEmail());
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(emailRequestDto.getEmail());
        simpleMailMessage.setFrom(env.getProperty("spring.mail.username"));
        StringBuilder textBuilder = new StringBuilder();

        if (emailType == EmailType.JOIN) {
            simpleMailMessage.setSubject(AUTH_TOKEN_SUBJECT);
            textBuilder.append("이메일 임시 인증 토큰은 ").append(auth).append("입니다.");
        } else if (emailType == EmailType.FIND_PW){
            simpleMailMessage.setSubject(FIND_PASSWORD_SUBJECT);
            textBuilder.append("인증 코드는 [").append(auth).append("]입니다.");
        }
        simpleMailMessage.setText(textBuilder.toString());
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.error("{} 쿼리 시도 중 발생 {},", emailRequestDto.getEmail(), e);
        }
    }

    @Override
    public void checkAuthToken(EmailCheckRequestDto emailCheckRequestDto) {
        String token = (String) redisTemplate.opsForValue().get(RedisKey.EMAIL_AUTH_TOKEN_PREFIX + emailCheckRequestDto.getEmail());
        if (token == null || !token.equals(emailCheckRequestDto.getToken())) {
            throw new EmailException(EmailExceptionMessage.DATA_NOT_FOUND);
        }
        createAuthSuccessToken(emailCheckRequestDto.getEmail());
    }


    private void createAuthSuccessToken(String email) {
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + email, "true", Duration.ofMillis(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_EXPIRATION_TIME));
    }

    private String createAuthToken(String email) {
        String tempTokenValue = generateRandomNumbers(EMAIL_TOKEN_LENGTH);
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_TOKEN_PREFIX + email, tempTokenValue, Duration.ofMillis(RedisKey.EMAIL_AUTH_TOKEN_EXPIRATION_TIME));
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

package com.reminiscence.email.controller;

import com.reminiscence.email.EmailType;
import com.reminiscence.email.dto.EmailCheckRequestDto;
import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.email.service.EmailService;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.EmailResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email/auth")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("")
    public ResponseEntity<Response> sendAuthToken(@Valid @RequestBody EmailRequestDto emailRequestDto){
        emailService.storeAuthToken(emailRequestDto, EmailType.JOIN);
        return new ResponseEntity<>(Response.of(EmailResponseMessage.EMAIL_SEND_SUCCESS),HttpStatus.OK);
    }

    @PostMapping("/check")
    public ResponseEntity<Response> checkAuthToken(@Valid @RequestBody EmailCheckRequestDto emailCheckRequestDto){
        emailService.checkAuthToken(emailCheckRequestDto);
        return new ResponseEntity<>(Response.of(EmailResponseMessage.EMAIL_AUTH_SUCCESS),HttpStatus.OK);
    }
}

package com.reminiscence.social.dto;

import com.reminiscence.social.type.SnsType;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SocialLoginRequest {
    @NotNull
    private SnsType snsType;
    @NotNull
    private String code;
}
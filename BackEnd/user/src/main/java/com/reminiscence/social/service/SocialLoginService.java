package com.reminiscence.social.service;

import com.reminiscence.social.dto.SocialAuthResponse;
import com.reminiscence.social.dto.SocialUserResponse;
import com.reminiscence.social.type.SnsType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    SnsType getServiceName();
    SocialAuthResponse getAccessToken(String authorizationCode);
    SocialUserResponse getUserInfo(String accessToken);
}
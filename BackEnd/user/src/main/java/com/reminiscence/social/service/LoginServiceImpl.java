package com.reminiscence.social.service;

import com.reminiscence.social.dto.SocialAuthResponse;
import com.reminiscence.social.dto.SocialUserResponse;
import com.reminiscence.social.type.SnsType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Component
@Qualifier("defaultLoginService")
public class LoginServiceImpl implements SocialLoginService {
    @Override
    public SnsType getServiceName() {
        return SnsType.NORMAL;
    }

    @Override
    public SocialAuthResponse getAccessToken(String authorizationCode) {
        return null;
    }

    @Override
    public SocialUserResponse getUserInfo(String accessToken) {
        return null;
    }
}
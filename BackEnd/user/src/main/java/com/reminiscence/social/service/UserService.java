package com.reminiscence.social.service;

import com.reminiscence.domain.Member;
import com.reminiscence.exception.customexception.MemberException;
import com.reminiscence.exception.message.MemberExceptionMessage;
import com.reminiscence.member.repository.MemberRepository;
import com.reminiscence.social.dto.*;
import com.reminiscence.social.type.SnsType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final List<SocialLoginService> loginServices;
    private final MemberRepository memberRepository;
    public LoginResponse doSocialLogin(SocialLoginRequest request) throws SQLException {
        SocialLoginService loginService = this.getLoginService(request.getSnsType());

        SocialAuthResponse socialAuthResponse = loginService.getAccessToken(request.getCode());

        SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (memberRepository.findByEmail(socialUserResponse.getEmail()) == null) {
            this.joinUser(
                    UserJoinRequest.builder()
                            .userId(socialUserResponse.getId())
                            .userEmail(socialUserResponse.getEmail())
                            .userName(socialUserResponse.getName())
                            .snsType(request.getSnsType())
                            .build()
            );
        }

        Member member = memberRepository.findByEmail(socialUserResponse.getId());
                if(member == null) throw new MemberException(MemberExceptionMessage.DATA_NOT_FOUND);

        return LoginResponse.builder()
                .id(member.getId())
                .build();
    }

    private UserJoinResponse joinUser(UserJoinRequest userJoinRequest) {
        Member member = memberRepository.save(
                Member.builder()
                        .email(userJoinRequest.getUserId())
                        .snsType(String.valueOf(userJoinRequest.getSnsType()))
                        .name(userJoinRequest.getUserName())
                        .build()
        );

        return UserJoinResponse.builder()
                .id(member.getId())
                .build();
    }

    private SocialLoginService getLoginService(SnsType snsType){
        for (SocialLoginService loginService: loginServices) {
            if (snsType.equals(loginService.getServiceName())) {
                log.info("login service name: {}", loginService.getServiceName());
                return loginService;
            }
        }
        return new LoginServiceImpl();
    }

    public UserResponse getUser(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberExceptionMessage.DATA_NOT_FOUND));

        return UserResponse.builder()
                .id(member.getId())
                .userEmail(member.getEmail())
                .userName(member.getName())
                .build();
    }
}
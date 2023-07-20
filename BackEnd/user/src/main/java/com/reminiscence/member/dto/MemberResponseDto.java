package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.RoleType;
import lombok.Builder;

public class MemberResponseDto {

    private String email;
    private String password;
    private String nickname;
    private RoleType roleType;
    private char genderFm;
    private int age;
    private String name;
    private String profile;
    private String snsId;
    private String snsType;
    private char personalAgreement;

    @Builder
    public MemberResponseDto(String email, String password, String nickname, RoleType roleType, char genderFm, int age, String name, String profile, String snsId, String snsType, char personalAgreement) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.snsId = snsId;
        this.snsType = snsType;
        this.personalAgreement = personalAgreement;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .roleType(roleType)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .personalAgreement(personalAgreement)
                .build();
    }
}

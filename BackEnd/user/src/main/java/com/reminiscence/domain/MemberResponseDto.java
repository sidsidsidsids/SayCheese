package com.reminiscence.domain;

import lombok.Builder;

public class MemberResponseDto {

    private String email;
    private String password;
    private String nickname;
    private char genderFm;
    private int age;
    private String name;
    private String profile;
    private String snsId;
    private String snsType;
    private char personalAgreement;

    @Builder
    public MemberResponseDto(String email, String password, String nickname, char genderFm, int age, String name, String profile, String snsId, String snsType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.snsId = snsId;
        this.snsType = snsType;
        this.personalAgreement = 'T';
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
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

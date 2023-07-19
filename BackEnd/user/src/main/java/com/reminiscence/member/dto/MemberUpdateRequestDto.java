package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequestDto {
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
    public MemberUpdateRequestDto(String email, String password, String nickname, char genderFm, int age, String name, String profile, String snsId, String snsType) {
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

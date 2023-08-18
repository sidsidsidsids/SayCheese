package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class MemberInfoUpdateRequestDto {

    private String password;
    private String nickname;
    private Character genderFm;
    private Integer age;
    private String name;
    private String profile;
    private String snsId;
    private String snsType;
    private Character personalAgreement;

    @Builder
    public MemberInfoUpdateRequestDto(String password, String nickname, Character genderFm, Integer age, String name, String profile, String snsId, String snsType) {
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

package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class MemberInfoUpdateRequestDto {

    @Column(name="password", nullable = false)
    private String password;
    @Column(name="nickname", nullable = false)
    private String nickname;
    @Column(name = "gender_fm")
    private char genderFm;
    @Column(name="age")
    private int age;
    @Column(name="name")
    private String name;
    @Column(name="profile")
    private String profile;
    @Column(name = "sns_id")
    private String snsId;
    @Column(name = "sns_type")
    private String snsType;
    @Column(name = "personal_agreement_yn")
    private char personalAgreement;

    @Builder
    public MemberInfoUpdateRequestDto(String password, String nickname, char genderFm, int age, String name, String profile, String snsId, String snsType) {
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

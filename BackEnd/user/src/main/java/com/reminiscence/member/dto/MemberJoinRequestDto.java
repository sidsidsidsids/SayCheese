package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class MemberJoinRequestDto {
    private String email;
    private String password;
    private String nickname;
    private Role role = Role.Member;
    @Column(name = "gender_fm")
    private char genderFm;
    private int age;
    private String name;
    private String profile;
    @Column(name = "sns_id")
    private String snsId;
    @Column(name = "sns_type")
    private String snsType;
    @Column(name = "personal_agreement_yn")
    private char personalAgreement;

    @Builder
    public MemberJoinRequestDto(String email, String password, String nickname, char genderFm, int age, String name, String profile, String snsId, String snsType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = Role.Member;
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
                .role(role)
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

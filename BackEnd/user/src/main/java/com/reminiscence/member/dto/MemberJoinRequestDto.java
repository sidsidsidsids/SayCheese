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
    private char genderFm;
    private int age;
    private String name;
    private String profile;
    private String snsId;
    private String snsType;

    @Builder
    public MemberJoinRequestDto(String email, String password, String nickname, char genderFm, int age, String name, String profile, String snsId, String snsType) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.snsId = snsId;
        this.snsType = snsType;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .delYn('N')
                .snsId(snsId)
                .snsType(snsType)
                .personalAgreement('T')
                .build();
    }
}

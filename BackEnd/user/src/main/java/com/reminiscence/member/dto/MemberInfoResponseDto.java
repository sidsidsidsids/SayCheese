package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberInfoResponseDto {

    private String email;
    private String nickname;
    private Character genderFm;
    private Integer age;
    private String name;
    private String profile;
    private String snsId;
    private String snsType;
    private Character personalAgreement;

    @Builder
    public MemberInfoResponseDto(String email, String nickname, Character genderFm, Integer age, String name, String profile, String snsId, String snsType, Character personalAgreement) {
        this.email = email;
        this.nickname = nickname;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
        this.snsId = snsId;
        this.snsType = snsType;
        this.personalAgreement = personalAgreement;
    }
}

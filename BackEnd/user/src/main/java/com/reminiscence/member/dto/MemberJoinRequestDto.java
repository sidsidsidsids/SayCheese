package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class MemberJoinRequestDto {

//    @Email(message="이메일 형식이 아닙니다.")
    private String email;
//    @Pattern(regexp = "/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/", message="비밀번호를 특수문자 포함 8자 이상으로 입력해주세요.")
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

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

    @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message ="이메일 양식이 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*\\d).{8,}$",
            message = "비밀번호를 영문자, 특수문자, 숫자를 포함한 8자 이상으로 입력해주세요.")
    private String password;
    private String nickname;
    private Character genderFm;
    private Integer age;
    private String name;
    private String profile;

    @Builder
    public MemberJoinRequestDto(String email, String password, String nickname, Character genderFm, Integer age, String name, String profile) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.genderFm = genderFm;
        this.age = age;
        this.name = name;
        this.profile = profile;
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
                .personalAgreement('T')
                .build();
    }
}

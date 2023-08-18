package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {
    @Email(regexp = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", message ="이메일 양식이 맞지 않습니다")
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;
    private String password;

    @Builder
    public MemberLoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}

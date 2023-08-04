package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Getter
@NoArgsConstructor
public class MemberLoginRequestDto {
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

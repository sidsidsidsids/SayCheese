package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
import lombok.Builder;

import javax.persistence.Column;

public class MemberUpdatePasswordRequestDto {
    @Column(name="password", nullable = false)
    private String password;

    @Builder
    public MemberUpdatePasswordRequestDto(String password) {
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .password(password)
                .build();
    }
}

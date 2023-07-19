package com.reminiscence.domain;

import lombok.Builder;

public class MemberUpdatePasswordDto {
    private String password;

    @Builder
    public MemberUpdatePasswordDto(String password) {
        this.password = password;
    }

    public Member toEntity() {
        return Member.builder()
                .password(password)
                .build();
    }
}

package com.reminiscence.member.dto;

import com.reminiscence.domain.Member;
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

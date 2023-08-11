package com.reminiscence.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Getter
@AllArgsConstructor
public class MemberNicknameResponseDto {

    @Null
    private String nickname;

}

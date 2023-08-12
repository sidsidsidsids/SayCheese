package com.reminiscence.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdatePasswordRequestDto {

    private String email;
    private String newPassword;
    private String passwordConfirm;
}

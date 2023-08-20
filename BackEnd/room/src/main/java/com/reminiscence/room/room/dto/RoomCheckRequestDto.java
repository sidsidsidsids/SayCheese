package com.reminiscence.room.room.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class RoomCheckRequestDto {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}

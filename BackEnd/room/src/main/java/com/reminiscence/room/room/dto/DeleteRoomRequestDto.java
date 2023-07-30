package com.reminiscence.room.room.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class DeleteRoomRequestDto {
    @NotBlank(message = "roomCode는 필수 입력 값입니다.")
    private String roomCode;
}

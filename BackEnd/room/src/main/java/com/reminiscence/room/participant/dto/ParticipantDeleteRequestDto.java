package com.reminiscence.room.participant.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ParticipantDeleteRequestDto {
    @NotBlank(message = "roomCode는 필수 입력 값입니다.")
    private String roomCode;
}

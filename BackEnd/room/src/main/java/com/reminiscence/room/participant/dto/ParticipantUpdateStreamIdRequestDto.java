package com.reminiscence.room.participant.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ParticipantUpdateStreamIdRequestDto {
    @NotBlank(message = "스트리밍 아이디를 입력해주세요.")
    private String streamId;
}

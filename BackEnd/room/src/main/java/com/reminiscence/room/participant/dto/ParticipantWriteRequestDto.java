package com.reminiscence.room.participant.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ParticipantWriteRequestDto {
    @NotBlank(message = "방장 여부를 입력해주세요.")
    private Character ownerYn;
    @NotBlank(message = "방 코드를 입력해주세요.")
    private String roomCode;
    @NotBlank(message = "스트림 아이디를 입력해주세요.")
    private String streamId;

}

package com.reminiscence.room.participant.dto;

import lombok.Getter;

@Getter
public class ParticipantWriteRequestDto {
    private String nickname;
    private String ownerYn;
    private String roomCode;

}

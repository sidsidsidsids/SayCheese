package com.reminiscence.room.participant.dto;

import lombok.Getter;

@Getter
public class ParticipantRoomUserResponseDto {
    Long memberId;

    public ParticipantRoomUserResponseDto(Long memberId){
        this.memberId = memberId;
    }
}

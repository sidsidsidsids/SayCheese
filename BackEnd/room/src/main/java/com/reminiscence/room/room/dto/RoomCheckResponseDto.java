package com.reminiscence.room.room.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomCheckResponseDto {
    private Character existParticipantYn;

    public RoomCheckResponseDto(Character existParticipantYn) {
        this.existParticipantYn = existParticipantYn;
    }
}

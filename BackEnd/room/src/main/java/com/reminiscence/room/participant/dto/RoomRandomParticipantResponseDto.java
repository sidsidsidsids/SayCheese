package com.reminiscence.room.participant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomRandomParticipantResponseDto {
    private String streamId;

    @Builder
    public RoomRandomParticipantResponseDto(String streamId) {
        this.streamId = streamId;
    }
}

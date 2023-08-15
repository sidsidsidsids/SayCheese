package com.reminiscence.room.participant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DummyUpdateConnectionYnParticipantRequestDto {
    private String streamId;

    @Builder
    public DummyUpdateConnectionYnParticipantRequestDto(String streamId) {
        this.streamId = streamId;
    }
}

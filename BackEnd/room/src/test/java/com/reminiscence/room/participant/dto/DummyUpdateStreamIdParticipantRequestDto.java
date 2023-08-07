package com.reminiscence.room.participant.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class DummyUpdateStreamIdParticipantRequestDto {
    private String streamId;

    @Builder
    public DummyUpdateStreamIdParticipantRequestDto(String streamId) {
        this.streamId = streamId;
    }
}

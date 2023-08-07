package com.reminiscence.room.participant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DummyUpdateConnectionYnParticipantRequestDto {
    private String nickname;

    @Builder
    public DummyUpdateConnectionYnParticipantRequestDto(String nickname) {
        this.nickname = nickname;
    }
}

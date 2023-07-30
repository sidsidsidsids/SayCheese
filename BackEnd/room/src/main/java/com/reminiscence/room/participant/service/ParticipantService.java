package com.reminiscence.room.participant.service;

import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;

public interface ParticipantService {

    public void writeParticipant(ParticipantWriteRequestDto requestDto, Long memberId);
    public void deleteParticipant(Long memberId, String roomCode);
}

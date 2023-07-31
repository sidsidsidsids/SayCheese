package com.reminiscence.room.participant.service;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;

public interface ParticipantService {

    public void writeParticipant(ParticipantWriteRequestDto requestDto, UserDetail userDetail);
    public void deleteParticipant(Long memberId, String roomCode);
}

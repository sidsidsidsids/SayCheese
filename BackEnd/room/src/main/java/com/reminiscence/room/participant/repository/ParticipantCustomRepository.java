package com.reminiscence.room.participant.repository;

import com.reminiscence.room.domain.Participant;

import java.util.Optional;

public interface ParticipantCustomRepository {
        Optional<Participant> findByMemberNickname(String nickname);
        Long countByRoomId(Long roomId);
}

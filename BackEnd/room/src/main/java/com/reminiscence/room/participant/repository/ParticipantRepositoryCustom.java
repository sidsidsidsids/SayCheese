package com.reminiscence.room.participant.repository;

import com.reminiscence.room.domain.Participant;

import java.util.Optional;

public interface ParticipantRepositoryCustom {
        Optional<Participant> findByNicknameAndRoomId(String nickname, Long roomId);
        Long countByRoomId(Long roomId);
}

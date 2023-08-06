package com.reminiscence.room.participant.repository;


import com.reminiscence.room.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantCustomRepository {
    Optional<Participant> findByMemberIdAndRoomId(Long memberId, Long roomId);
    Optional<Participant> findByOwnerYnAndRoomId(Character ownerYn, Long roomId);
    void deleteByRoomId(Long roomId);
    void deleteAllByRoomId(Long roomId);
}

package com.reminiscence.room.participant.repository;

;
import com.reminiscence.room.domain.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, ParticipantRepositoryCustom {
    Optional<Participant> findByOwnerYnAndRoomId(Character ownerYn, Long roomId);
    @Modifying
    @Query("delete from Participant p where p.room.id = :roomId")
    void deleteAllByRoomId(@Param("roomId") Long roomId);
}

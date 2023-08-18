package com.reminiscence.room.participant.repository;

import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;
import com.reminiscence.room.participant.dto.RoomRandomParticipantResponseDto;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepositoryCustom {
        Optional<List<ParticipantRoomUserResponseDto>> findUserByRoomID(Long roomId);
        Optional<List<RoomRandomParticipantResponseDto>> findByRandomParticipant(Long roomId);
        Optional<Participant> findByStreamIdAndRoomId(String nickname, Long roomId);
        Optional<Participant> findByMemberIdAndRoomId(Long memberId, Long roomId);
        Optional<Participant> findByMemberIdAndConnectionY(Long memberId);
        Optional<Participant> findByOwnerYAndRoomId(Long roomId);
        Long countByRoomId(Long roomId);
}

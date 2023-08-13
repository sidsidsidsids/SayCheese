package com.reminiscence.room.participant.repository;

import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;

import javax.servlet.http.Part;
import java.util.List;
import java.util.Optional;

public interface ParticipantRepositoryCustom {
        Optional<List<ParticipantRoomUserResponseDto>> findUserByRoomID(Long roomId);
        Optional<Participant> findByNicknameAndRoomId(String nickname, Long roomId);
        Optional<Participant> findByMemberIdAndRoomId(Long memberId, Long roomId);
        Optional<Participant> findByMemberIdAndConnectionY(Long memberId);
        Long countByRoomId(Long roomId);
}

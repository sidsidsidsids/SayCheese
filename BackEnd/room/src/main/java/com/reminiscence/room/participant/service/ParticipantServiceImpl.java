package com.reminiscence.room.participant.service;

import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.exception.customexception.MemberException;
import com.reminiscence.room.exception.customexception.ParticipantException;
import com.reminiscence.room.exception.customexception.RoomException;
import com.reminiscence.room.exception.message.MemberExceptionMessage;
import com.reminiscence.room.exception.message.ParticipantExceptionMessage;
import com.reminiscence.room.exception.message.RoomExceptionMessage;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements ParticipantService{
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    @Override
    public void writeParticipant(ParticipantWriteRequestDto requestDto, Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        member.orElseThrow(() ->
                new MemberException(MemberExceptionMessage.NOT_FOUND_MEMBER));

        Optional<Room> room = roomRepository.findByRoomCode(requestDto.getRoomCode());
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Participant participant = Participant.builder()
                .nickname(requestDto.getNickname())
                .ownerYn(requestDto.getOwnerYn())
                .member(member.get())
                .room(room.get())
                .build();
        participantRepository.save(participant);
    }

    @Override
    public void deleteParticipant(Long memberId, String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<Participant> participant = participantRepository.findByMemberIdAndRoomId(memberId, room.get().getId());
        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        participantRepository.delete(participant.get());
    }
}

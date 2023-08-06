package com.reminiscence.room.participant.service;

import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.exception.customexception.ParticipantException;
import com.reminiscence.room.exception.customexception.RoomException;
import com.reminiscence.room.exception.message.ParticipantExceptionMessage;
import com.reminiscence.room.exception.message.RoomExceptionMessage;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.participant.dto.ParticipantUpdateStreamIdRequestDto;
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
    public void writeParticipant(ParticipantWriteRequestDto requestDto, Member member) {
        Optional<Room> room = roomRepository.findByRoomCode(requestDto.getRoomCode());
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Participant participant = Participant.builder()
                .ownerYn(requestDto.getOwnerYn())
                .member(member)
                .room(room.get())
                .connectionYn('Y')
                .build();
        participantRepository.save(participant);
    }

    @Transactional
    @Override
    public void updateParticipantOwner(Long memberId, String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<Participant> ownerParticipant =
                participantRepository.findByOwnerYnAndRoomId('Y', room.get().getId());
        ownerParticipant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_OWNER_PARTICIPANT));

        Optional<Participant> participant =
                participantRepository.findByMemberIdAndRoomId(memberId, room.get().getId());
        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        ownerParticipant.get().updateOwnerYn('N');
        participant.get().updateOwnerYn('Y');
    }

    @Override
    public void updateParticipantStreamId(
            Member member,
            String roomCode,
            ParticipantUpdateStreamIdRequestDto requestDto) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_SESSION));
        Optional<Participant> participant =
                participantRepository.findByMemberIdAndRoomId(member.getId(), room.get().getId());

        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        participant.get().updateStreamId(requestDto.getStreamId());
    }

    @Override
    public void updateParticipantConnectionFail(String nickname, String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<Participant> participant = participantRepository.findByNicknameAndRoomId(nickname, room.get().getId());
        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        participant.get().updateConnectionYn('N');
    }

    @Override
    public void updateParticipantConnectionSuccess(Long memberId, String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<Participant> participant = participantRepository.findByMemberIdAndRoomId(memberId,room.get().getId());
        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        participant.get().updateConnectionYn('Y');
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

    @Override
    public void deleteAllParticipant(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(() ->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        participantRepository.deleteAllByRoomId(room.get().getId());
    }
}

package com.reminiscence.room.participant.service;

import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.exception.customexception.ParticipantException;
import com.reminiscence.room.exception.customexception.RoomException;
import com.reminiscence.room.exception.message.ParticipantExceptionMessage;
import com.reminiscence.room.exception.message.RoomExceptionMessage;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;
import com.reminiscence.room.participant.dto.ParticipantUpdateStreamIdRequestDto;
import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;
import com.reminiscence.room.participant.dto.RoomRandomParticipantResponseDto;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantServiceImpl implements ParticipantService{
    private final ParticipantRepository participantRepository;
    private final RoomRepository roomRepository;


    @Override
    public List<ParticipantRoomUserResponseDto> getRoomUserParticipant(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(() ->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));
        List<ParticipantRoomUserResponseDto> participants = participantRepository.findUserByRoomID(room.get().getId()).orElse(null);
        return participants;
    }

    @Override
    public List<RoomRandomParticipantResponseDto> getRandomParticipant(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<List<RoomRandomParticipantResponseDto>> randomParticipant = participantRepository.findByRandomParticipant(room.get().getId());
        randomParticipant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));
        int size = randomParticipant.get().size();

        if(size== 1){
            for(int i=0;i<3;i++){
                randomParticipant.get().add(randomParticipant.get().get(0));
            }
        }else if(size < 4){
            boolean[] check = new boolean[size];
            int count = size;
            while(true){
                if(count == 4){
                    break;
                }
                int random = (int) ((Math.random() * (size) + 1)) - 1;
                if(check[random]) {
                    continue;
                }else{
                    check[random] = true;
                    randomParticipant.get().add(randomParticipant.get().get(random));
                }
                count++;
            }
        }
        for(int i=0;i<4;i++){
            System.out.println(randomParticipant.get().get(i).getStreamId());
        }

        return randomParticipant.get();
    }
    @Transactional
    @Override
    public void writeParticipant(ParticipantWriteRequestDto requestDto, Member member) {
        Optional<Room> room = roomRepository.findByRoomCode(requestDto.getRoomCode());
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Participant owner = participantRepository.findByOwnerYAndRoomId(room.get().getId()).orElse(null);
        char ownerYn = 'Y';
        if(owner != null){
            ownerYn = 'N';
        }
        Participant participant = Participant.builder()
                .ownerYn(ownerYn)
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
    @Transactional
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
        participant.get().updateConnectionYn('Y');
    }
    @Transactional
    @Override
    public void updateParticipantConnectionFail(String streamId, String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        Optional<Participant> participant = participantRepository.findByStreamIdAndRoomId(streamId, room.get().getId());
        participant.orElseThrow(()->
                new ParticipantException(ParticipantExceptionMessage.NOT_FOUND_PARTICIPANT));

        participant.get().updateConnectionYn('N');
    }

    @Transactional
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
    @Transactional
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

    @Transactional
    @Override
    public void deleteAllParticipant(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(() ->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        participantRepository.deleteAllByRoomId(room.get().getId());
    }
}

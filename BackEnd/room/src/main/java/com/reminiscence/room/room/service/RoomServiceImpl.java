package com.reminiscence.room.room.service;

import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.exception.customexception.RoomException;
import com.reminiscence.room.exception.message.RoomExceptionMessage;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.dto.RoomInfoResponseDto;
import com.reminiscence.room.room.dto.WriteRoomRequestDto;
import com.reminiscence.room.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;
    private final WebClient webClient;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void writeRoom(WriteRoomRequestDto requestDto) {
        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.encryptPassword(encryptedPassword);
        roomRepository.save(requestDto.toEntity());
    }


    @Override
    public RoomInfoResponseDto readRoomInfo(String roomCode) {
        Optional<RoomInfoResponseDto> roomInfo = roomRepository.findRoomInfoByRoomCode(roomCode);
        roomInfo.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));
        return roomInfo.get();
    }

    @Override
    public void checkRoomConnection(Long memberId) {
        Participant participant =
                participantRepository.findByMemberIdAndConnectionY(memberId).orElse(null);

        if(participant != null){
            throw new RoomException(RoomExceptionMessage.ALREADY_IN_ROOM);
        }
    }

    @Override
    public void checkRoomPassword(String roomCode, String password) {
        if(!checkSession(roomCode)){
            throw new RoomException(RoomExceptionMessage.NOT_FOUND_SESSION);
        };
        Optional<Room> room = roomRepository.findByRoomCodeAndStartN(roomCode);
        room.orElseThrow(() -> new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));
        if(!passwordEncoder.matches(password, room.get().getPassword())){
            throw new RoomException(RoomExceptionMessage.NOT_MATCH_PASSWORD);
        }
        Long participantCount =
                participantRepository.countByRoomId(room.get().getId());
        if(participantCount > room.get().getMaxCount()){
            throw new RoomException(RoomExceptionMessage.ROOM_IS_FULL);
        }
    }

    @Transactional
    @Override
    public void updateRoomStart(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(()->
                new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        room.get().updateRoomStart();
    }

    @Transactional
    @Override
    public void deleteRoom(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(
                () -> new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));
        participantRepository.deleteAllByRoomId(room.get().getId());
        roomRepository.delete(room.get());
    }

    public boolean checkSession(String roomCode) {
        try{
            webClient.get()
                    .uri("openvidu/api/sessions/" + roomCode)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}

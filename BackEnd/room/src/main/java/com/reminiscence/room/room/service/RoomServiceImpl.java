package com.reminiscence.room.room.service;

import com.reminiscence.room.domain.Room;
import com.reminiscence.room.exception.customexception.RoomException;
import com.reminiscence.room.exception.message.RoomExceptionMessage;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.dto.WriteRoomRequestDto;
import com.reminiscence.room.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final ParticipantRepository participantRepository;

    @Override
    public void writeRoom(WriteRoomRequestDto requestDto) {
        roomRepository.save(requestDto.toEntity());
    }

    @Override
    public void deleteRoom(String roomCode) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(() -> new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));
        participantRepository.deleteByRoomId(room.get().getId());
        roomRepository.deleteByRoomCode(roomCode);
    }

    @Override
    public void checkRoomPassword(String roomCode, String password) {
        Optional<Room> room = roomRepository.findByRoomCode(roomCode);
        room.orElseThrow(() -> new RoomException(RoomExceptionMessage.NOT_FOUND_ROOM));

        if(!room.get().getPassword().equals(password)){
            throw new RoomException(RoomExceptionMessage.NOT_MATCH_PASSWORD);
        }
    }
}

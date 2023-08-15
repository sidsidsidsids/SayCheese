package com.reminiscence.room.room.repository;

import com.reminiscence.room.domain.Room;
import com.reminiscence.room.room.dto.RoomInfoResponseDto;

import java.util.Optional;


public interface RoomRepositoryCustom {
    Optional<RoomInfoResponseDto> findRoomInfoByRoomCode(String roomCode);
    Optional<Room> findByRoomCodeAndStartN(String roomCode);
}

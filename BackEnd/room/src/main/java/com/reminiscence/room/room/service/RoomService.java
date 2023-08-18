package com.reminiscence.room.room.service;

import com.reminiscence.room.room.dto.RoomInfoResponseDto;
import com.reminiscence.room.room.dto.WriteRoomRequestDto;

public interface RoomService {
    RoomInfoResponseDto readRoomInfo(String roomCode);
    void checkRoomPassword(String roomCode, String password);
    void checkRoomConnection(Long memberId);
    void writeRoom(WriteRoomRequestDto requestDto);
    void updateRoomStart(String roomCode);
    void deleteRoom(String roomCode);
}

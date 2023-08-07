package com.reminiscence.room.room.service;

import com.reminiscence.room.room.dto.WriteRoomRequestDto;

public interface RoomService {
    void writeRoom(WriteRoomRequestDto requestDto);
    void deleteRoom(String roomCode);
    void checkRoomPassword(String roomCode, String password);
    void checkRoomConnection(Long memberId);
}

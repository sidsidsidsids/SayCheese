package com.reminiscence.room.room.controller;

import com.reminiscence.room.message.Response;
import com.reminiscence.room.message.custom_message.RoomMessage;
import com.reminiscence.room.room.dto.DeleteRoomRequestDto;
import com.reminiscence.room.room.dto.RoomCheckPwdRequestDto;
import com.reminiscence.room.room.dto.WriteRoomRequestDto;
import com.reminiscence.room.room.service.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/check/{roomCode}")
    public ResponseEntity<Response> checkRoomPassword(
            @PathVariable String roomCode,
            @RequestBody @Valid RoomCheckPwdRequestDto requestDto){
        roomService.checkRoomPassword(roomCode, requestDto.getPassword());
        return new ResponseEntity(Response.of(RoomMessage.ROOM_PASSWORD_CHECK_SUCCESS), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> writeRoom(
            @RequestBody @Valid WriteRoomRequestDto requestDto) {
        roomService.writeRoom(requestDto);
        return new ResponseEntity(Response.of(RoomMessage.ROOM_WRITE_SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteRoom(
            @RequestBody @Valid DeleteRoomRequestDto requestDto) {
        roomService.deleteRoom(requestDto.getRoomCode());
        return new ResponseEntity(Response.of(RoomMessage.ROOM_DELETE_SUCCESS), HttpStatus.OK);
    }
}

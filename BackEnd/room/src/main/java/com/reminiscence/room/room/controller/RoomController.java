package com.reminiscence.room.room.controller;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.domain.Role;
import com.reminiscence.room.message.Response;
import com.reminiscence.room.message.custom_message.RoomMessage;
import com.reminiscence.room.participant.service.ParticipantService;
import com.reminiscence.room.room.dto.DeleteRoomRequestDto;
import com.reminiscence.room.room.dto.RoomCheckRequestDto;
import com.reminiscence.room.room.dto.RoomCheckResponseDto;
import com.reminiscence.room.room.dto.WriteRoomRequestDto;
import com.reminiscence.room.room.service.RoomService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;
    private final ParticipantService participantService;

    @PostMapping("/check/{roomCode}")
    public ResponseEntity<RoomCheckResponseDto> checkRoomPassword(
            @PathVariable String roomCode,
            @RequestBody @Valid RoomCheckRequestDto requestDto,
            @AuthenticationPrincipal UserDetail userDetail) {
        roomService.checkRoomPassword(roomCode, requestDto.getPassword());
        if(userDetail == null){
            return new ResponseEntity(new RoomCheckResponseDto('N'), HttpStatus.OK);
        }else{
            if(userDetail.getMember().getRole() == Role.GUEST){
                participantService.updateParticipantConnectionSuccess(userDetail.getMember().getId(),roomCode);
            }
            return new ResponseEntity(new RoomCheckResponseDto('Y'), HttpStatus.OK);
        }
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

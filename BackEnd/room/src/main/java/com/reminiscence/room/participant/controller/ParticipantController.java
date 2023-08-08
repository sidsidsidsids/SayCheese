package com.reminiscence.room.participant.controller;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.message.Response;
import com.reminiscence.room.message.custom_message.ParticipantMessage;
import com.reminiscence.room.participant.dto.*;
import com.reminiscence.room.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/{roomCode}")
    public ResponseEntity<List<ParticipantRoomUserResponseDto>> readRoomUserParticipant(@PathVariable String roomCode){
        List<ParticipantRoomUserResponseDto> roomUserParticipant = participantService.getRoomUserParticipant(roomCode);
        return new ResponseEntity(roomUserParticipant, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> writeParticipant(
            @RequestBody @Valid ParticipantWriteRequestDto requestDto,
            @AuthenticationPrincipal UserDetail userDetail) {
        participantService.writeParticipant(requestDto, userDetail.getMember());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_WRITE_SUCCESS), HttpStatus.OK);
    }
    @PutMapping("/{roomCode}/owner")
    public ResponseEntity<Response> updateParticipantOwner(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable("roomCode") String roomCode){
        participantService.updateParticipantOwner(userDetail.getMember().getId(), roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_OWNER_SUCCESS),HttpStatus.OK);
    }

    @PutMapping("/{roomCode}/streamId")
    public ResponseEntity<Response> updateParticipantStreamId(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable("roomCode") String roomCode,
            @RequestBody @Valid ParticipantUpdateStreamIdRequestDto requestDto){
        participantService.updateParticipantStreamId(userDetail.getMember(), roomCode, requestDto);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_STREAM_ID_SUCCESS),HttpStatus.OK);
    }

    @PutMapping("/fail/connection/{roomCode}")
    public ResponseEntity<Response> updateParticipantConnectionFail(
            @PathVariable("roomCode") String roomCode,
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid ParticipantUpdateConnectionRequestDto requestDto
    ){
        participantService.updateParticipantConnectionFail(requestDto.getNickname(), roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_CONNECTION_SUCCESS),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteParticipant(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid ParticipantDeleteRequestDto requestDto){
        participantService.deleteParticipant(userDetail.getMember().getId(), requestDto.getRoomCode());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_DELETE_SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping("/all/{roomCode}")
    public ResponseEntity<Response> deleteAllParticipant(
            @PathVariable("roomCode") String roomCode){
        participantService.deleteAllParticipant(roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_DELETE_ALL_SUCCESS),HttpStatus.OK);
    }
}

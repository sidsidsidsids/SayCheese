package com.reminiscence.room.participant.controller;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.message.Response;
import com.reminiscence.room.message.custom_message.ParticipantMessage;
import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;
import com.reminiscence.room.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<Response> writeParticipant(
            @RequestBody @Valid ParticipantWriteRequestDto requestDto,
            @AuthenticationPrincipal UserDetail userDetail) {
        participantService.writeParticipant(requestDto, userDetail.getMember().getId());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_WRITE_SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteParticipant(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid ParticipantWriteRequestDto requestDto){
        participantService.deleteParticipant(userDetail.getMember().getId(), requestDto.getRoomCode());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_DELETE_SUCCESS), HttpStatus.OK);
    }
}

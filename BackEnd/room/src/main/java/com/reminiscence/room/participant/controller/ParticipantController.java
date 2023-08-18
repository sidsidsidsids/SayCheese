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
@RequestMapping("/api/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    /**
     * 방 참가한 회원 목록 조회
     * @pathVariable
     * roomCode : JWT 토큰으로 인증된 사용자 정보
     *
     * @return
     * Response : roomUserParticipant
     *          memberId : 회원 아이디
     */

    @GetMapping("/{roomCode}")
    public ResponseEntity<List<ParticipantRoomUserResponseDto>> readRoomUserParticipant(@PathVariable String roomCode){
        List<ParticipantRoomUserResponseDto> roomUserParticipant = participantService.getRoomUserParticipant(roomCode);
        return new ResponseEntity(roomUserParticipant, HttpStatus.OK);
    }

    @GetMapping("random/{roomCode}")
    public ResponseEntity<List<RoomRandomParticipantResponseDto>> readRandomParticipant(
            @PathVariable("roomCode") String roomCode){
        List<RoomRandomParticipantResponseDto> responseDto = participantService.getRandomParticipant(roomCode);
        return new ResponseEntity(responseDto, HttpStatus.OK);

    }

    /**
     * 회원 추가 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : 참가자 정보
     *         roomCode : 페이지 번호
     *         ownerYn : 방장 여부
     * @return
     * Response : HttpStatus.OK
     */

    @PostMapping
    public ResponseEntity<Response> writeParticipant(
            @RequestBody @Valid ParticipantWriteRequestDto requestDto,
            @AuthenticationPrincipal UserDetail userDetail) {
        participantService.writeParticipant(requestDto, userDetail.getMember());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_WRITE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 방장 변경 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @pathVariable
     * roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @PutMapping("/{roomCode}/owner")
    public ResponseEntity<Response> updateParticipantOwner(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable("roomCode") String roomCode){
        participantService.updateParticipantOwner(userDetail.getMember().getId(), roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_OWNER_SUCCESS),HttpStatus.OK);
    }
    /**
     * 참가자 스트림 아이디 변경 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : 스트림 정보
     *            streamId : 스트림 아이디
     * @pathVariable
     * roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @PutMapping("/{roomCode}/streamId")
    public ResponseEntity<Response> updateParticipantStreamId(
            @AuthenticationPrincipal UserDetail userDetail,
            @PathVariable("roomCode") String roomCode,
            @RequestBody @Valid ParticipantUpdateStreamIdRequestDto requestDto){
        participantService.updateParticipantStreamId(userDetail.getMember(), roomCode, requestDto);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_STREAM_ID_SUCCESS),HttpStatus.OK);
    }
    /**
     * 참가자 연결 여부 변경 API
     * @pathVariable
     * roomCode : 방 코드
     * @param
     * requestDto : 참여자 정보
     *            nickname : 연결 변경자 닉네임
     * @return
     * Response : HttpStatus.OK
     */
    @PutMapping("/fail/connection/{roomCode}")
    public ResponseEntity<Response> updateParticipantConnectionFail(
            @PathVariable("roomCode") String roomCode,
            @RequestBody @Valid ParticipantUpdateConnectionRequestDto requestDto
    ){
        participantService.updateParticipantConnectionFail(requestDto.getStreamId(), roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_CHANGE_CONNECTION_SUCCESS),HttpStatus.OK);
    }
    /**
     * 참가자 삭제 API
     * @param
     * userDetail : 방 코드
     * @param
     * requestDto : 방 정보
     *            roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @DeleteMapping
    public ResponseEntity<Response> deleteParticipant(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid ParticipantDeleteRequestDto requestDto){
        participantService.deleteParticipant(userDetail.getMember().getId(), requestDto.getRoomCode());
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_DELETE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 방 참가자 전부 삭제 API
     * @pathVariable
     * roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @DeleteMapping("/all/{roomCode}")
    public ResponseEntity<Response> deleteAllParticipant(
            @PathVariable("roomCode") String roomCode){
        participantService.deleteAllParticipant(roomCode);
        return new ResponseEntity(Response.of(ParticipantMessage.PARTICIPANT_DELETE_ALL_SUCCESS),HttpStatus.OK);
    }
}

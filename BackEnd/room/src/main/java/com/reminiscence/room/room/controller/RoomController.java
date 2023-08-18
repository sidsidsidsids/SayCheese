package com.reminiscence.room.room.controller;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.domain.Role;
import com.reminiscence.room.message.Response;
import com.reminiscence.room.message.custom_message.RoomMessage;
import com.reminiscence.room.participant.service.ParticipantService;
import com.reminiscence.room.room.dto.*;
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


    /**
     * 방 정보 조회 API
     * @param
     * roomCode : 방 코드
     * @return
     * Response : roomInfoResponseDto
     *          specification : 방 규격
     *          mode : 방 모드
     */
    @GetMapping
    public ResponseEntity<RoomInfoResponseDto> readRoomInfo(
            @Valid @RequestParam("roomCode") String roomCode) {
        RoomInfoResponseDto roomInfoResponseDto = roomService.readRoomInfo(roomCode);
        return new ResponseEntity(roomInfoResponseDto, HttpStatus.OK);
    }

    /**
     * 방 접근 확인 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @return
     * Response : HttpStatus.OK
     */
    @PostMapping("/check")
    public ResponseEntity<Response> checkConnection(
            @AuthenticationPrincipal UserDetail userDetail){
        if(userDetail == null){
            return new ResponseEntity(Response.of(RoomMessage.ROOM_CONNECTION_ABLE), HttpStatus.OK);
        }
        roomService.checkRoomConnection(userDetail.getMember().getId());
        return new ResponseEntity(Response.of(RoomMessage.ROOM_CONNECTION_ABLE), HttpStatus.OK);
    }
    /**
     * 방 비밀번호 확인 API
     * @pathVariable
     * roomCode : 방 코드
     * @param
     * requestDto : 방 정보
     *             password : 방 비밀번호
     * @return
     * Response : RoomCheckResponseDto
     *          existParticipantYn : 참여자 기존 참가자 여부
     */
    @PostMapping("/check/{roomCode}")
    public ResponseEntity<Response> checkRoomPassword(
            @PathVariable String roomCode,
            @RequestBody @Valid RoomCheckRequestDto requestDto) {
        roomService.checkRoomPassword(roomCode, requestDto.getPassword());
        return new ResponseEntity(Response.of(RoomMessage.ROOM_CHECK_SUCCESS), HttpStatus.OK);
    }
    /**
     * 방 생성 API
     * @param
     * requestDto : 방 정보
     *            password : 방 비밀번호
     *            maxCount : 방 최대 인원
     *            mode : 방 모드
     *            roomCode : 방 코드
     *            specification : 방 규격
     * @return
     * Response : HttpStatus.OK
     */
    @PostMapping
    public ResponseEntity<Response> writeRoom(
            @RequestBody @Valid WriteRoomRequestDto requestDto) {
        roomService.writeRoom(requestDto);
        return new ResponseEntity(Response.of(RoomMessage.ROOM_WRITE_SUCCESS), HttpStatus.OK);
    }


    /**
     * 방 시작 여부 변경 API
     * @pathVariable
     * roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @PutMapping("{roomCode}/start")
    public ResponseEntity<Response> updateRoomStart(
            @PathVariable("roomCode") String roomCode){
        roomService.updateRoomStart(roomCode);
        return new ResponseEntity(Response.of(RoomMessage.ROOM_START_SUCCESS), HttpStatus.OK);
    }

    /**
     * 방 삭제 API
     * @pathVariable
     * roomCode : 방 코드
     * @return
     * Response : HttpStatus.OK
     */
    @DeleteMapping("/{roomCode}")
    public ResponseEntity<Response> deleteRoom(
            @PathVariable("roomCode") String roomCode) {
        roomService.deleteRoom(roomCode);
        return new ResponseEntity(Response.of(RoomMessage.ROOM_DELETE_SUCCESS), HttpStatus.OK);
    }
}

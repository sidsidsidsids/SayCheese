package com.reminiscence.room.participant.service;

import com.reminiscence.room.config.auth.UserDetail;
import com.reminiscence.room.domain.Member;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;
import com.reminiscence.room.participant.dto.ParticipantUpdateStreamIdRequestDto;
import com.reminiscence.room.participant.dto.ParticipantWriteRequestDto;
import com.reminiscence.room.participant.dto.RoomRandomParticipantResponseDto;

import java.util.List;

public interface ParticipantService {
    List<ParticipantRoomUserResponseDto> getRoomUserParticipant(String roomCode);
    List<RoomRandomParticipantResponseDto> getRandomParticipant(String roomCode);
    void writeParticipant(ParticipantWriteRequestDto requestDto, Member member);
    void updateParticipantOwner(Long memberId, String roomCode);
    void updateParticipantStreamId(Member member,String roomCode, ParticipantUpdateStreamIdRequestDto requestDto);
    void updateParticipantConnectionFail(String nickname, String roomCode);
    void updateParticipantConnectionSuccess(Long memberId, String roomCode);
    void deleteParticipant(Long memberId, String roomCode);
    void deleteAllParticipant(String roomCode);

}

package com.reminiscence.room.participant;

import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;
import com.reminiscence.room.participant.dto.RoomRandomParticipantResponseDto;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ParticipantJpaTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ParticipantRepository participantRepository;

    @Test
    @DisplayName("방 유저 참가자 조회 테스트")
    public void readRoomUserParticipantTest(){
        // given
        String roomCode = "sessionA";

        // when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);
        List<ParticipantRoomUserResponseDto> userByRoomID = participantRepository.findUserByRoomID(room.getId()).orElse(null);

        // then
        assertNotNull(userByRoomID);
        for(ParticipantRoomUserResponseDto response : userByRoomID){
            System.out.println(response.getMemberId());
        }
        assertEquals(2, userByRoomID.size());
    }

    @Test
    @DisplayName("참가자 생성 테스트")
    public void createParticipantTest(){
        // given
        Long memberId = 2L;
        String roomCode = "sessionA";

        Member member = memberRepository.findById(memberId).orElse(null);
        assertNotNull(member);

        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);

        // when
        Participant participant = Participant.builder()
                .ownerYn('N')
                .member(member)
                .room(room)
                .build();
        participantRepository.save(participant);
        Participant findParticipant = participantRepository.findById(participant.getId()).orElse(null);

        // then
        assertNotNull(findParticipant);
        assertEquals(participant.getId(), findParticipant.getId());
        assertEquals(member.getNickname(),findParticipant.getMember().getNickname());
        assertEquals(memberId, findParticipant.getMember().getId());
        assertEquals(roomCode, findParticipant.getRoom().getRoomCode());
        assertEquals(participant.getOwnerYn(), findParticipant.getOwnerYn());
    }

    @Test
    @DisplayName("방 랜덤 스트림 ID 조회 테스트")
    public void RoomRandomParticipantStreamIdTest(){
        //given
        String roomCode = "sessionB";
        //when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);

        List<RoomRandomParticipantResponseDto> participants = participantRepository.findByRandomParticipant(room.getId()).orElse(null);

        //then
        assertNotNull(participants);
        assertEquals(1, participants.size());
    }

    @Test
    @DisplayName("방장 변경 테스트")
    public void updateRoomOwnerTest(){
        //given
        Long memberId = 3L;
        String roomCode = "sessionA";

        //when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);

        Participant ownerParticipant =
                participantRepository.findByOwnerYnAndRoomId('Y', room.getId()).orElse(null);
        assertNotNull(ownerParticipant);

        Participant curParticipant =
                participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(curParticipant);

        ownerParticipant.updateOwnerYn('N');
        curParticipant.updateOwnerYn('Y');
        //then
        Participant updatedOwnerParticipant =
                participantRepository.findById(ownerParticipant.getId()).orElse(null);
        assertNotNull(updatedOwnerParticipant);

        Participant updatedParticipant = participantRepository.findById(curParticipant.getId()).orElse(null);
        assertNotNull(updatedParticipant);

        assertEquals('N', updatedOwnerParticipant.getOwnerYn());
        assertEquals('Y', updatedParticipant.getOwnerYn());

    }

    @Test
    @DisplayName("참가자 스트림 ID 변경 테스트")
     public void updateParticipantStreamId(){
        //given
        Long memberId = 3L;
        String roomCode = "sessionA";
        String streamId = "TestStreamId";

        //when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);
        assertEquals(roomCode, room.getRoomCode());

        Participant participant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(participant);

        participant.updateStreamId(streamId);
        //then
        Participant updatedParticipant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertEquals(streamId, updatedParticipant.getStreamId());
        assertEquals(participant.getId(), updatedParticipant.getId());
        assertEquals(memberId, updatedParticipant.getMember().getId());
    }

    @Test
    @DisplayName("참가자 연결 취소 변경 테스트")
    public void updateParticipantConnectionNTest(){
        //given
        Long memberId = 3L;
        String roomCode = "sessionA";
        Character connectionYn = 'N';

        //when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);
        assertEquals(roomCode, room.getRoomCode());

        Participant participant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(participant);

        participant.updateConnectionYn(connectionYn);
        //then
        Participant updateParticipant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(updateParticipant);
        assertEquals(connectionYn, updateParticipant.getConnectionYn());
        assertEquals(memberId, updateParticipant.getMember().getId());
        assertEquals(room.getId(), updateParticipant.getRoom().getId());
    }

    @Test
    @DisplayName("참가자 연결 성공 변경 테스트")
    public void updateParticipantConnectionYTest(){
        //given
        Long memberId = 5L;
        String roomCode = "sessionA";
        Character connectionYn = 'Y';

        //when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);
        assertEquals(roomCode, room.getRoomCode());

        Participant participant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(participant);

        participant.updateConnectionYn(connectionYn);
        //then
        Participant updateParticipant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(updateParticipant);
        assertEquals(connectionYn, updateParticipant.getConnectionYn());
        assertEquals(memberId, updateParticipant.getMember().getId());
        assertEquals(room.getId(), updateParticipant.getRoom().getId());
    }


    @Test
    @DisplayName("참가자 삭제 테스트")
    public void deleteParticipantTest(){
        // given
        Long memberId = 1L;
        String roomCode = "sessionA";

        // when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);

        Participant participant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        assertNotNull(participant);

        participantRepository.delete(participant);
        Participant findParticipant = participantRepository.findByMemberIdAndRoomId(memberId, room.getId()).orElse(null);
        // then
        assertNull(findParticipant);
    }

    @Test
    @DisplayName("참가자 전체 삭제 테스트")
    public void deleteAllParticipantTest(){
        // given
        String roomCode = "sessionA";

        // when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        assertNotNull(room);

        participantRepository.deleteAllByRoomId(room.getId());
        // then
        assertEquals(3, participantRepository.count());
    }

}

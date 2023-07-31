package com.reminiscence.room.participant;

import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.participant.repository.ParticipantRepository;
import com.reminiscence.room.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
                .ownerYn("N")
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

}

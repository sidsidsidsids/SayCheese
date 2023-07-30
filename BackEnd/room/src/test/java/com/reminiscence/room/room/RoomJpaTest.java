package com.reminiscence.room.room;

import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.room.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoomJpaTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    @DisplayName("방 비밀번호 확인 테스트")
    public void checkPasswordTest(){
        // given
        String roomCode = "sessionA";
        String password = "1234";
        // when
        Room room = roomRepository.findByRoomCode(roomCode).orElse(null);
        // then
        assertNotNull(room);
        assertEquals(password, room.getPassword());
    }

    @Test
    @DisplayName("방 생성 테스트")
    public void createRoomTest(){
        // given
        Room room = Room.builder()
                .password("1234")
                .roomCode("tussle")
                .mode(Mode.GAME)
                .maxCount(4)
                .endDate(LocalDateTime.now())
                .specification("Gradle")
                .build();
        // when
        roomRepository.save(room);
        Room findRoom = roomRepository.findById(room.getId()).orElse(null);
        // then
        assertNotNull(findRoom);
        assertEquals(room.getId(), findRoom.getId());
        assertEquals(room.getPassword(), findRoom.getPassword());
        assertEquals(room.getRoomCode(), findRoom.getRoomCode());
        assertEquals(room.getMode(), findRoom.getMode());
        assertEquals(room.getMaxCount(), findRoom.getMaxCount());
        assertEquals(room.getEndDate(), findRoom.getEndDate());
        assertEquals(room.getSpecification(), findRoom.getSpecification());
    }
    @Test
    @DisplayName("방 삭제 테스트")
    public void deleteRoomTest(){
        // given
        String roomCode = "sessionA";

        // when
        roomRepository.deleteByRoomCode(roomCode);
        Room findRoom = roomRepository.findByRoomCode(roomCode).orElse(null);
        // then
        assertNull(findRoom);
    }
}

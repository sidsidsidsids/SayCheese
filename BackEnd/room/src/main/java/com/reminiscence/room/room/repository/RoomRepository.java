package com.reminiscence.room.room.repository;

import com.reminiscence.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> , RoomRepositoryCustom{
    public Optional<Room> findByRoomCode(String roomCode);
}

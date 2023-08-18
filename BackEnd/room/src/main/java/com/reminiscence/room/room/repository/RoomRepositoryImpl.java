package com.reminiscence.room.room.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.room.domain.QRoom;
import com.reminiscence.room.domain.Room;
import com.reminiscence.room.room.dto.RoomInfoResponseDto;

import javax.persistence.EntityManager;
import java.util.Optional;

public class RoomRepositoryImpl implements RoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public RoomRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public Optional<RoomInfoResponseDto> findRoomInfoByRoomCode(String roomCode) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(RoomInfoResponseDto.class,
                        QRoom.room.specification.as("specification"),
                        QRoom.room.mode.as("mode")
                        ))
                .from(QRoom.room)
                .where(roomCodeEq(roomCode))
                .fetchFirst());
    }

    @Override
    public Optional<Room> findByRoomCodeAndStartN(String roomCode) {
        return Optional.ofNullable(queryFactory
                .select(QRoom.room)
                .from(QRoom.room)
                .where(roomCodeEq(roomCode), startNEq())
                .fetchFirst());
    }
    private BooleanExpression roomCodeEq(String roomCode){
        return QRoom.room.roomCode.eq(roomCode);
    }
    private BooleanExpression startNEq(){
        return QRoom.room.startYn.eq('N');
    }
}

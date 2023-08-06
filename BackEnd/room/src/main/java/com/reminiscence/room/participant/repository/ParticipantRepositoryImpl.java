package com.reminiscence.room.participant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.QParticipant;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ParticipantRepositoryImpl implements ParticipantCustomRepository{
    private final JPAQueryFactory queryFactory;

    public ParticipantRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Participant> findByMemberNickname(String nickname) {
        return Optional.ofNullable(
                queryFactory.select(QParticipant.participant)
                        .where(eqNickname(nickname))
                        .fetchOne());
    }

    @Override
    public Long countByRoomId(Long roomId) {
        return queryFactory.select(QParticipant.participant)
                .where(eqRoomId(roomId),eqConnection())
                .fetchCount();
    }

    public BooleanExpression eqNickname(String nickname){
        return QParticipant.participant.member.nickname.eq(nickname);
    }
    public BooleanExpression eqRoomId(Long roomId){
        return QParticipant.participant.room.id.eq(roomId);
    }
    public BooleanExpression eqConnection(){
        return QParticipant.participant.connectionYn.eq('Y');
    }
}

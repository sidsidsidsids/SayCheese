package com.reminiscence.room.participant.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.QParticipant;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ParticipantRepositoryImpl implements ParticipantRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public ParticipantRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<Participant> findByMemberIdAndConnectionY(Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(QParticipant.participant)
                .from(QParticipant.participant)
                .where(eqMemberId(memberId),eqConnection())
                .fetchFirst());
    }

    @Override
    public Optional<Participant> findByNicknameAndRoomId(String nickname,Long roomId) {
        return Optional.ofNullable(
                queryFactory.select(QParticipant.participant)
                        .from(QParticipant.participant)
                        .where(eqNickname(nickname),eqRoomId(roomId))
                        .fetchFirst());
    }

    @Override
    public Optional<Participant> findByMemberIdAndRoomId(Long memberId, Long roomId) {
        return Optional.ofNullable(
                queryFactory.select(QParticipant.participant)
                        .from(QParticipant.participant)
                        .where(eqMemberId(memberId),eqRoomId(roomId))
                        .fetchFirst());
    }

    @Override
    public Long countByRoomId(Long roomId) {
        return queryFactory.select(QParticipant.participant)
                .from(QParticipant.participant)
                .where(eqRoomId(roomId),eqConnection())
                .fetchCount();
    }

    public BooleanExpression eqNickname(String nickname){
        return QParticipant.participant.member.nickname.eq(nickname);
    }
    public BooleanExpression eqMemberId(Long memberId){
        return QParticipant.participant.member.id.eq(memberId);
    }
    public BooleanExpression eqRoomId(Long roomId){
        return QParticipant.participant.room.id.eq(roomId);
    }
    public BooleanExpression eqConnection(){
        return QParticipant.participant.connectionYn.eq('Y');
    }
}

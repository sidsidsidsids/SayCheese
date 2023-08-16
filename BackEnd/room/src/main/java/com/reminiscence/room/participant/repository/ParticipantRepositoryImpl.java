package com.reminiscence.room.participant.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.room.domain.Participant;
import com.reminiscence.room.domain.QMember;
import com.reminiscence.room.domain.QParticipant;
import com.reminiscence.room.domain.Role;
import com.reminiscence.room.participant.dto.ParticipantRoomUserResponseDto;
import com.reminiscence.room.participant.dto.RoomRandomParticipantResponseDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


public class ParticipantRepositoryImpl implements ParticipantRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    public ParticipantRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<List<ParticipantRoomUserResponseDto>> findUserByRoomID(Long roomId) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(ParticipantRoomUserResponseDto.class,
                        QMember.member.id.as("memberId")
                ))
                .from(QParticipant.participant)
                .join(QParticipant.participant.member, QMember.member)
                .where(eqRoomId(roomId), eqMemberId(), eqConnection())
                .fetch());
    }

    @Override
    public Optional<List<RoomRandomParticipantResponseDto>> findByRandomParticipant(Long roomId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(RoomRandomParticipantResponseDto.class,
                        QParticipant.participant.streamId.as("streamId")))
                .from(QParticipant.participant)
                .where(eqRoomId(roomId),eqConnection())
                .orderBy(random())
                .fetch());
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
    public Optional<Participant> findByOwnerYAndRoomId(Long roomId) {
        return Optional.ofNullable(queryFactory
                .select(QParticipant.participant)
                .from(QParticipant.participant)
                .where(eqRoomId(roomId),eqOwner())
                .fetchFirst());
    }

    @Override
    public Optional<Participant> findByStreamIdAndRoomId(String streamId,Long roomId) {
        return Optional.ofNullable(
                queryFactory.select(QParticipant.participant)
                        .from(QParticipant.participant)
                        .where(eqStreamId(streamId),eqRoomId(roomId))
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

    private BooleanExpression eqStreamId(String streamId){
        return QParticipant.participant.streamId.eq(streamId);
    }
    private BooleanExpression eqMemberId(Long memberId){
        return QParticipant.participant.member.id.eq(memberId);
    }
    private BooleanExpression eqRoomId(Long roomId){
        return QParticipant.participant.room.id.eq(roomId);
    }
    private BooleanExpression eqConnection(){
        return QParticipant.participant.connectionYn.eq('Y');
    }
    private BooleanExpression eqMemberId(){
        return QParticipant.participant.member.role.notIn(Role.GUEST);
    }
    private BooleanExpression eqOwner(){
        return QParticipant.participant.ownerYn.eq('Y');
    }
    private OrderSpecifier<Double> random() {
        return Expressions.numberTemplate(Double.class, "function('rand')").asc();
    }
}

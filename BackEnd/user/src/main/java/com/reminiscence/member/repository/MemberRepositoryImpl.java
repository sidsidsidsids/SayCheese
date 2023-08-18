package com.reminiscence.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;


import com.reminiscence.domain.Member;
import com.reminiscence.domain.QMember;
import com.reminiscence.member.dto.MemberSearchResponseDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberCustomRepository{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MemberSearchResponseDto> searchMembers(String key) {
        QMember member = QMember.member;
        return queryFactory.select(Projections.constructor(MemberSearchResponseDto.class, member.email, member.nickname))
                .from(member)
                .where(member.email.likeIgnoreCase("%" + key + "%")
                        .or(member.nickname.likeIgnoreCase("%" + key + "%")))
                .fetch();
    }
}

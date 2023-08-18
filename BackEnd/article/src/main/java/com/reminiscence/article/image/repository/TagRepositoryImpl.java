package com.reminiscence.article.image.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.QTag;
import com.reminiscence.article.image.dto.RandomTagResponseDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class TagRepositoryImpl implements TagRepositoryCustom{

    private final JPAQueryFactory queryFactory;


    public TagRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<List<RandomTagResponseDto>> findRandomTags() {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(RandomTagResponseDto.class,
                        QTag.tag.id.as("id"),
                        QTag.tag.name.as("tag")))
                .from(QTag.tag)
                .orderBy(orderByRandom())
                .limit(4)
                .fetch());
    }

    public OrderSpecifier<Double> orderByRandom(){
        return Expressions.numberTemplate(Double.class, "function('rand')").asc();
    }
}

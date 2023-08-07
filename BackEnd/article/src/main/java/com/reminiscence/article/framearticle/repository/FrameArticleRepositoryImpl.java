package com.reminiscence.article.framearticle.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.ObjectUtils.isEmpty;

public class FrameArticleRepositoryImpl implements FrameArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FrameArticleRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 로그인한 유저 프레임 게시판 항목들(좋아요 포함) 반환
    @Override
    public Optional<List<FrameArticleListResponseDto>> findMemberFrameArticles(Pageable page, Long memberId, String authorSubject) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(FrameArticleListResponseDto.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("nickname"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)
                                        , (loverMemberIdEq(memberId)))
                                .limit(1), "loverYn")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(frameArticleAuthorLikeIgnoreCase(authorSubject)
                        .or(frameArticleSubjectLikeIgnoreCase(authorSubject)))
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

    // 비회원(로그인 하지 않은) 유저의 프레임 게시판 항목들(좋아요 불포함) 반환
    @Override
    public Optional<List<FrameArticleListResponseDto>> findNonMemberFrameArticles(Pageable page, String authorSubject) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(FrameArticleListResponseDto.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("nickname")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(frameArticleAuthorLikeIgnoreCase(authorSubject)
                        .or(frameArticleSubjectLikeIgnoreCase(authorSubject)))
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

//    // 검색어를 통해 저자명 혹은 제목을 조회하여 검색 결과 반환
//    @Override
//    public Optional<List<FrameArticleListResponseDto>> findMemberFrameArticleAllBySearchWord(Pageable page, String authorSubject, Long memberId) {
//        return Optional.ofNullable(queryFactory.select(Projections.constructor(FrameArticleListResponseDto.class,
//                        QFrameArticle.frameArticle.id.as("articleId"),
//                        QFrameArticle.frameArticle.subject.as("subject"),
//                        QFrame.frame.link.as("frameLink"),
//                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
//                                .from(QLover.lover)
//                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
//                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
//                        QFrameArticle.frameArticle.member.nickname.as("nickname"),
//                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
//                                .from(QLover.lover)
//                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)
//                                        , (loverMemberIdEq(memberId)))
//                                .limit(1), "loverYn")
//                ))
//                .join(QFrameArticle.frameArticle.member, QMember.member).fetchJoin()
//                .join(QFrameArticle.frameArticle.frame, QFrame.frame).fetchJoin()
//                .where(frameArticleAuthorLikeIgnoreCase(authorSubject)
//                        .or(frameArticleSubjectLikeIgnoreCase(authorSubject)))
//                .orderBy(getOrderSpecifiers(page))
//                .limit(page.getPageSize())
//                .fetch());
//    }
//
//    // 검색어를 통해 저자명 혹은 제목을 조회하여 검색 결과 반환 (비회원)
//    @Override
//    public Optional<List<FrameArticleListResponseDto>> findNonMemberFrameArticleAllBySearchWord(Pageable page, String authorSubject) {
//        return Optional.ofNullable(queryFactory.select(Projections.constructor(FrameArticleListResponseDto.class,
//                        QFrameArticle.frameArticle.id.as("articleId"),
//                        QFrameArticle.frameArticle.subject.as("subject"),
//                        QFrame.frame.link.as("frameLink"),
//                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
//                                .from(QLover.lover)
//                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
//                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
//                        QFrameArticle.frameArticle.member.nickname.as("nickname")))
//                .join(QFrameArticle.frameArticle.member, QMember.member).fetchJoin()
//                .join(QFrameArticle.frameArticle.frame, QFrame.frame).fetchJoin()
//                .where(frameArticleAuthorLikeIgnoreCase(authorSubject)
//                        .or(frameArticleSubjectLikeIgnoreCase(authorSubject)))
//                .orderBy(getOrderSpecifiers(page))
//                .limit(page.getPageSize())
//                .fetch());
//    }

    private OrderSpecifier[] getOrderSpecifiers(Pageable page) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(page.getSort())) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "random":
                        OrderSpecifier<Double> random = Expressions.numberTemplate(Double.class, "function('rand')").asc();
                        ORDERS.add(random);
                        break;
                    case "createdDate":
                        OrderSpecifier<?> orderDate = new OrderSpecifier(direction, QFrameArticle.frameArticle.createdDate);
                        ORDERS.add(orderDate);
                        break;
                    case "lover":
                        NumberPath<Long> aliasCount = Expressions.numberPath(Long.class, "loverCnt");

                        OrderSpecifier<?> orderLover = new OrderSpecifier(direction, aliasCount);
                        ORDERS.add(orderLover);
                        break;
                    default:
                        break;
                }
            }
            return ORDERS.toArray(new OrderSpecifier[ORDERS.size()]);
        }
        return null;
    }

    private BooleanExpression loverMemberIdEq(Long memberId) {
        return QLover.lover.memberId.eq(memberId);
    }

    private BooleanExpression frameArticleAuthorLikeIgnoreCase(String frameArticleAuthor) {
        return QFrameArticle.frameArticle.member.nickname.likeIgnoreCase("%" + frameArticleAuthor + "%");
    }

    private BooleanExpression frameArticleSubjectLikeIgnoreCase(String frameArticleSubject) {
        return QFrameArticle.frameArticle.subject.likeIgnoreCase("%" + frameArticleSubject + "%");
    }

}

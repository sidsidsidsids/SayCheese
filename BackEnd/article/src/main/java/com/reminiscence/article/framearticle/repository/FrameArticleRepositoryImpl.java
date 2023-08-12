package com.reminiscence.article.framearticle.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.framearticle.dto.FrameArticleReadRequestDto;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class FrameArticleRepositoryImpl implements FrameArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FrameArticleRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // 로그인한 유저 검색어에 해당하는 프레임 게시판 항목들(좋아요 포함) 반환
    // 공개된 프레임 게시글과 회원이 작성한 게시글 모두 반환 (공개 여부 포함)
    @Override
    public Page<FrameArticleVo> findMemberFrameArticles(Pageable pageable, Long memberId, String authorSubject, String frameSpec) {
        List<FrameArticleVo> list = queryFactory
                .select(Projections.constructor(FrameArticleVo.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("author"),
                        QFrame.frame.open_yn.as("openYn"),
                        QFrame.frame.frameSpecification.as("frameSpecification"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)
                                        , (loverMemberIdEq(memberId)))
                                .limit(1), "loverYn"),
                        isMyFrame(memberId).as("isMine")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(searchWord(authorSubject),
                        ((frameIsOpened())
                                .or(isMyFrame(memberId))), searchFrameSpec(frameSpec))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(QFrameArticle.frameArticle)
                .where(searchWord(authorSubject),
                        ((frameIsOpened())
                                .or(isMyFrame(memberId))))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    // 비회원(로그인 하지 않은) 유저의 검색어에 해당하는 프레임 게시판 항목들(좋아요 불포함) 반환
    // 공개된 개시글만 반환 (공개 여부 포함)
    @Override
    public Page<FrameArticleVo> findNonMemberFrameArticles(Pageable pageable, String authorSubject, String frameSpec) {
        List<FrameArticleVo> list = queryFactory
                .select(Projections.constructor(FrameArticleVo.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("author"),
                        QFrame.frame.open_yn.as("openYn"),
                        QFrame.frame.frameSpecification.as("frameSpecification")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(searchWord(authorSubject),
                        (frameIsOpened()))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(QFrameArticle.frameArticle)
                .where(searchWord(authorSubject),
                        (frameIsOpened()))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);

    }

    @Override
    public Page<FrameArticleVo> findMyFrameArticles(Pageable pageable, Long memberId, String authorSubject, String frameSpec) {
        List<FrameArticleVo> list = queryFactory
                .select(Projections.constructor(FrameArticleVo.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("author"),
                        QFrame.frame.open_yn.as("openYn"),
                        QFrame.frame.frameSpecification.as("frameSpecification"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)
                                        , (loverMemberIdEq(memberId)))
                                .limit(1), "loverYn"),
                        isMyFrame(memberId).as("isMine")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(isMyFrame(memberId), searchWord(authorSubject))
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(Wildcard.count)
                .from(QFrameArticle.frameArticle)
                .where(isMyFrame(memberId), searchWord(authorSubject))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public FrameArticleVo findMemberFrameArticle(FrameArticleReadRequestDto frameArticleReadRequestDto) {
        FrameArticleVo frameArticleVo = queryFactory
                .select(Projections.constructor(FrameArticleVo.class,
                        QFrameArticle.frameArticle.id.as("articleId"),
                        QFrameArticle.frameArticle.subject.as("subject"),
                        QFrame.frame.link.as("frameLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)), "loverCnt"),
                        QFrameArticle.frameArticle.createdDate.as("createdDate"),
                        QFrameArticle.frameArticle.member.nickname.as("author"),
                        QFrame.frame.open_yn.as("openYn"),
                        QFrame.frame.frameSpecification.as("frameSpecification"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QFrameArticle.frameArticle.id)
                                        , (loverMemberIdEq(frameArticleReadRequestDto.getUserDetail().getMember().getId())))
                                .limit(1), "loverYn"),
                        isMyFrame(frameArticleReadRequestDto.getUserDetail().getMember().getId()).as("isMine")
                ))
                .from(QFrameArticle.frameArticle)
                .join(QFrameArticle.frameArticle.member, QMember.member)
                .join(QFrameArticle.frameArticle.frame, QFrame.frame)
                .where(QFrameArticle.frameArticle.id.eq(frameArticleReadRequestDto.getFrameArticleId()))
                .fetchOne();
        return frameArticleVo;
    }

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

    private BooleanExpression searchWord(String searchWord) {
        if (StringUtils.isBlank(searchWord) || searchWord == null) {
            return null;
        }
        return frameArticleAuthorLikeIgnoreCase(searchWord).or(frameArticleSubjectLikeIgnoreCase(searchWord));
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

    private BooleanExpression frameIsOpened() {
        return QFrame.frame.open_yn.eq('Y');
    }

    private BooleanExpression isMyFrame(Long memberId) {
        return QFrameArticle.frameArticle.member.id.eq(memberId);
    }

    private BooleanExpression searchFrameSpec(String frameSpecification) {
        if (frameSpecification.toString().equals("")) return QFrame.frame.frameSpecification.eq(FrameSpecification.VERTICAL).or(QFrame.frame.frameSpecification.eq(FrameSpecification.HORIZONTAL));
        return QFrame.frame.frameSpecification.eq(FrameSpecification.valueOf(frameSpecification));
    }
}


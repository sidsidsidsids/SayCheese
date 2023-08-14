package com.reminiscence.article.imagearticle.repository;

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
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.ExpressionUtils.count;
import static org.springframework.util.ObjectUtils.isEmpty;

public class ImageArticleRepositoryImpl implements ImageArticleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ImageArticleRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public Optional<List<ImageArticleListResponseDto>> findMemberCommonImageArticles(Pageable page, Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleListResponseDto.class,
                        QImageArticle.imageArticle.id.as("articleId"),
                        QImage.image.link.as("imageLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)), "loverCnt"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImageArticle.imageArticle.member.nickname.as("nickname"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)
                                        ,(loverMemberIdEq(memberId)))
                                .limit(1), "loverYn")
                ))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .where(memberDelNEq())
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

    @Override
    public Optional<List<ImageArticleListResponseDto>> findNonMemberCommonImageArticles(Pageable page) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleListResponseDto.class,
                        QImageArticle.imageArticle.id.as("articleId"),
                        QImage.image.link.as("imageLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)), "loverCnt"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImageArticle.imageArticle.member.nickname.as("nickname")
                ))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .where(memberDelNEq())
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

    @Override
    public Optional<List<ImageArticleListResponseDto>> findMemberTagImageArticles(Long tagId, Pageable page, Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleListResponseDto.class,
                        QImageArticle.imageArticle.id.as("articleId"),
                        QImage.image.link.as("imageLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)), "loverCnt"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImageArticle.imageArticle.member.nickname.as("nickname"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)
                                        ,(loverMemberIdEq(memberId)))
                                .limit(1), "loverYn")
                ))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .join(QImage.image.imageTags, QImageTag.imageTag)
                .join(QImageTag.imageTag.tag, QTag.tag)
                .where(tagIdEq(tagId), memberDelNEq())
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

    @Override
    public Optional<List<ImageArticleListResponseDto>> findNonMemberTagImageArticles(Long tagId, Pageable page) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleListResponseDto.class,
                        QImageArticle.imageArticle.id.as("articleId"),
                        QImage.image.link.as("imageLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(QImageArticle.imageArticle.id)), "loverCnt"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImageArticle.imageArticle.member.nickname.as("nickname")
                ))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .join(QImage.image.imageTags, QImageTag.imageTag)
                .join(QImageTag.imageTag.tag, QTag.tag)
                .where(tagIdEq(tagId),memberDelNEq())
                .orderBy(getOrderSpecifiers(page))
                .limit(page.getPageSize())
                .fetch());
    }

    @Override
    public Optional<ImageArticleDetailResponseDto> findMemberImageArticleDetailById(Long articleId, Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleDetailResponseDto.class,
                        memberIdEq(memberId).as("isMine"),
                        QImage.image.id.as("imageId"),
                        QMember.member.nickname.as("author"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImage.image.link.as("imgLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(articleId)),"loverCnt"),
                        ExpressionUtils.as(JPAExpressions.select(QLover.lover.id)
                                .from(QLover.lover)
                                .where(QLover.lover.article.id.eq(articleId),loverMemberIdEq(memberId))
                                .limit(1),"loverYn")))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .where(QImageArticle.imageArticle.id.eq(articleId))
                .fetchOne());
    }

    @Override
    public Optional<ImageArticleDetailResponseDto> findNonMemberImageArticleDetailById(Long articleId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageArticleDetailResponseDto.class,
                        QImage.image.id.as("imageId"),
                        QMember.member.nickname.as("name"),
                        QImageArticle.imageArticle.createdDate.as("createdDate"),
                        QImage.image.link.as("imgLink"),
                        ExpressionUtils.as(JPAExpressions.select(count(QLover.lover.id))
                                .from(QLover.lover)
                                .where(loverArticleIdEq(articleId)),"loverCnt")))
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member)
                .join(QImageArticle.imageArticle.image, QImage.image)
                .where(imageArticleIdEq(articleId))
                .fetchOne());
    }

    @Override
    public Optional<ImageArticle> findImageArticleOfAllById(Long articleId) {
        return Optional.ofNullable(queryFactory
                .select(QImageArticle.imageArticle)
                .from(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member).fetchJoin()
                .join(QImageArticle.imageArticle.image, QImage.image).fetchJoin()
                .where(QImageArticle.imageArticle.id.eq(articleId))
                .fetchOne());
    }

    @Override
    public Optional<ImageArticle> findImageArticleOfMemberById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(QImageArticle.imageArticle)
                .innerJoin(QImageArticle.imageArticle.member).fetchJoin()
                .where(imageArticleIdEq(id))
                .fetchOne());
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
                        OrderSpecifier<?> orderDate = new OrderSpecifier(direction, QImageArticle.imageArticle.createdDate);
                        ORDERS.add(orderDate);
                        break;
                    case "lover":
                        NumberPath<Long> aliasCount = Expressions.numberPath( Long.class, "loverCnt");

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

    private BooleanExpression loverMemberIdEq(Long memberId){
        return QLover.lover.memberId.eq(memberId);
    }
    private BooleanExpression loverArticleIdEq(Long articleId){
        return QLover.lover.article.id.eq(articleId);
    }
    private BooleanExpression tagIdEq(Long tagId) {
        return QTag.tag.id.eq(tagId);
    }
    private BooleanExpression memberIdEq(Long memberId) {
        return QMember.member.id.eq(memberId);
    }
    private BooleanExpression memberDelNEq() {
        return QMember.member.delYn.eq('N');
    }
    private BooleanExpression imageArticleIdEq(Long imageArticleId) {
        return QImageArticle.imageArticle.id.eq(imageArticleId);
    }
}

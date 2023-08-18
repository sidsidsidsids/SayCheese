package com.reminiscence.article.image.repository;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.image.vo.ImageVo;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ImageRepositoryImpl implements ImageRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    public ImageRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<List<ImageDeleteResponseDto>> findNonOwnerImage() {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(ImageDeleteResponseDto.class,
                        QImage.image.id.as("imageId"),
                        QImage.image.link.as("imageName"),
                        QImage.image.type.as("imageType")
                ))
                .from(QImage.image)
                .leftJoin(QImage.image.imageOwners, QImageOwner.imageOwner)
                .where(QImageOwner.imageOwner.imageOwnerKey.memberId.isNull())
                .fetch());
    }

    @Override
    public Optional<Page<ImageVo>> findRecentOwnerImage(Long memberId, Pageable pageable ) {

        List<ImageVo> images = queryFactory
                .select(Projections.constructor(ImageVo.class,
                        QImage.image.id.as("imageId"),
                        QImage.image.link.as("imageLink"),
                        QImage.image.createdDate.as("createDate"),
                        ExpressionUtils.as(
                                queryFactory
                                        .select(QImageArticle.imageArticle.id)
                                        .from(QImageArticle.imageArticle)
                                        .where(QImageArticle.imageArticle.image.id.eq(QImage.image.id)),
                                "articleId"),
                        ExpressionUtils.as(
                                queryFactory
                                        .select(QLover.lover.count())
                                        .from(QLover.lover)
                                        .where(QLover.lover.article.id.eq(QImage.image.imageArticle.id)),
                                "loverCnt")
                ))
                .from(QImageOwner.imageOwner)
                .join(QImageOwner.imageOwner.image, QImage.image)
                .leftJoin(QImageOwner.imageOwner.image.imageArticle, QImageArticle.imageArticle)
                .where(eqMemberId(memberId))
                .orderBy(QImageOwner.imageOwner.image.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count =  queryFactory.select(Wildcard.count)
                .from(QImageOwner.imageOwner)
                .where(eqMemberId(memberId))
                .fetchOne();

        return Optional.ofNullable(new PageImpl(images, pageable, count));
    }
    private BooleanExpression eqMemberId(Long memberId){
        return QImageOwner.imageOwner.imageOwnerKey.memberId.eq(memberId);
    }
}

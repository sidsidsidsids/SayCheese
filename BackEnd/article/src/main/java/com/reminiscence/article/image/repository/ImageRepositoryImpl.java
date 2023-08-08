package com.reminiscence.article.image.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.image.dto.OwnerImageListResponseDto;
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
                        QImage.image.link.as("imageName"),
                        QImage.image.type.as("imageType")
                ))
                .from(QImage.image)
                .leftJoin(QImage.image.imageOwners, QImageOwner.imageOwner)
                .where(QImageOwner.imageOwner.imageOwnerKey.memberId.isNull())
                .fetch());
    }

    @Override
    public Optional<Page<Image>> findRecentOwnerImage(Long memberId, Pageable pageable ) {
        List<Image> images = queryFactory
                .select(QImage.image)
                .from(QImageOwner.imageOwner)
                .join(QImageOwner.imageOwner.image, QImage.image)
                .where(eqMemberId(memberId))
                .orderBy(QImage.image.createdDate.desc())
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

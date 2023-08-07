package com.reminiscence.article.image.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.QImage;
import com.reminiscence.article.domain.QImageOwner;
import com.reminiscence.article.domain.QLover;
import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
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
    public Optional<List<OwnerImageResponseDto>> findRecentOwnerImage(Pageable page, Long memberId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(OwnerImageResponseDto.class,
                        QImage.image.id.as("imageId"),
                        QImage.image.link.as("imageLink"),
                        QImage.image.createdDate.as("createdDate")
                ))
                .from(QImageOwner.imageOwner)
                .join(QImageOwner.imageOwner.image, QImage.image)
                .where(QImageOwner.imageOwner.imageOwnerKey.memberId.eq(memberId))
                .orderBy(QImage.image.createdDate.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch());
    }
}

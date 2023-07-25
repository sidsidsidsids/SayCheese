package com.reminiscence.article.image.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.QImage;
import com.reminiscence.article.domain.QImageOwner;
import com.reminiscence.article.domain.QLover;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;

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
}

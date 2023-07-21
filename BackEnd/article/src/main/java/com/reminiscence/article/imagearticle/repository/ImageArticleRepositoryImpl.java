package com.reminiscence.article.imagearticle.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;

import javax.persistence.EntityManager;
import java.util.Optional;

public class ImageArticleRepositoryImpl implements ImageArticleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public ImageArticleRepositoryImpl(EntityManager entityManager){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<ImageArticle> findImageArticleOfAllById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member).fetchJoin()
                .join(QImageArticle.imageArticle.image, QImage.image).fetchJoin()
                .join(QImage.image.imageTags, QImageTag.imageTag).fetchJoin()
                .join(QImageTag.imageTag.tag, QTag.tag).fetchJoin()
                .where(QImageArticle.imageArticle.id.eq(id))
                .fetchOne());
    }

    @Override
    public Optional<ImageArticle> findImageArticleOfMemberById(Long id) {
        return Optional.ofNullable(queryFactory.selectFrom(QImageArticle.imageArticle)
                .innerJoin(QImageArticle.imageArticle.member).fetchJoin()
                .where(QImageArticle.imageArticle.id.eq(id))
                .fetchOne());
    }
}

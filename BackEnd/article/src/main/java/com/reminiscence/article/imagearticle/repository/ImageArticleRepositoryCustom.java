package com.reminiscence.article.imagearticle.repository;

import com.reminiscence.article.domain.ImageArticle;

import java.util.Optional;

public interface ImageArticleRepositoryCustom {
    public Optional<ImageArticle> findImageArticleOfAllById(Long id);
    public Optional<ImageArticle> findImageArticleOfMemberById(Long id);
}

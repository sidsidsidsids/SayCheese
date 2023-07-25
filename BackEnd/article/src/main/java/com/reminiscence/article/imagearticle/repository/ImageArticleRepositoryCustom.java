package com.reminiscence.article.imagearticle.repository;

import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ImageArticleRepositoryCustom {
    public Optional<List<ImageArticleListResponseDto>> findTagImageArticles(Long tagId, Pageable page);
    public Optional<List<ImageArticleListResponseDto>> findCommonImageArticles(Pageable page);
    public Optional<ImageArticleDetailResponseDto> findImageArticleDetailById(Long id);
    public Optional<ImageArticle> findImageArticleOfAllById(Long id);
    public Optional<ImageArticle> findImageArticleOfMemberById(Long id);
}

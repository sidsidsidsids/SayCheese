package com.reminiscence.article.imagearticle.repository;

import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ImageArticleRepositoryCustom {
    Optional<List<ImageArticleListResponseDto>> findMemberCommonImageArticles(Pageable page, Long memberId);
    Optional<List<ImageArticleListResponseDto>> findNonMemberCommonImageArticles(Pageable page);
    Optional<List<ImageArticleListResponseDto>> findMemberTagImageArticles(Long tagId, Pageable page, Long memberId);
    Optional<List<ImageArticleListResponseDto>> findNonMemberTagImageArticles(Long tagId, Pageable page);
    Optional<ImageArticleDetailResponseDto> findMemberImageArticleDetailById(Long articleId, Long memberId);
    Optional<ImageArticleDetailResponseDto> findNonMemberImageArticleDetailById(Long articleId);
    Optional<ImageArticle> findImageArticleOfAllById(Long articleId);
    Optional<ImageArticle> findImageArticleOfMemberById(Long id);
}

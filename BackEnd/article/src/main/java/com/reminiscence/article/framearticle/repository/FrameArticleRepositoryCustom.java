package com.reminiscence.article.framearticle.repository;

import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FrameArticleRepositoryCustom {
    Optional<List<FrameArticleListResponseDto>> findMemberFrameArticles(Pageable page, Long memberId);
    Optional<List<FrameArticleListResponseDto>> findNonMemberFrameArticles(Pageable page);
    Optional<FrameArticle> findFrameArticleOfAllById(Long id);
    Optional<FrameArticle> findFrameArticleOfMemberById(Long id);


}

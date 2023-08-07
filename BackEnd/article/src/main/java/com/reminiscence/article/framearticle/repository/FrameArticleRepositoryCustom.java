package com.reminiscence.article.framearticle.repository;

import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FrameArticleRepositoryCustom {
    Optional<List<FrameArticleListResponseDto>> findMemberFrameArticles(Pageable page, Long memberId, String authorSubject);
    Optional<List<FrameArticleListResponseDto>> findNonMemberFrameArticles(Pageable page, String authorSubject);
//    Optional<List<FrameArticle>> findFrameArticleAllBySearchWord(Pageable pageable, String authorSubject);

}

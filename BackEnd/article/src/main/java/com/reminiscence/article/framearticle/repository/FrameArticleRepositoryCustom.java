package com.reminiscence.article.framearticle.repository;

import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FrameArticleRepositoryCustom {
    Page<FrameArticleVo> findMemberFrameArticles(Pageable page, Long memberId, String authorSubject);
    Page<FrameArticleVo> findNonMemberFrameArticles(Pageable page, String authorSubject);
//    Optional<List<FrameArticle>> findFrameArticleAllBySearchWord(Pageable pageable, String authorSubject);

}

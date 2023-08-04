package com.reminiscence.article.framearticle.repository;

import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameArticleRepository extends JpaRepository<FrameArticle,Long> {
}

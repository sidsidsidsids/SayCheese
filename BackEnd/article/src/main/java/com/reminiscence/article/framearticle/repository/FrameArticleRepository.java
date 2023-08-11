package com.reminiscence.article.framearticle.repository;

import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FrameArticleRepository extends JpaRepository<FrameArticle,Long>, FrameArticleRepositoryCustom{
}

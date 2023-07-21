package com.reminiscence.article.imagearticle.repository;

import com.reminiscence.article.domain.ImageArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageArticleRepository extends JpaRepository<ImageArticle, Long>, ImageArticleRepositoryCustom {

}

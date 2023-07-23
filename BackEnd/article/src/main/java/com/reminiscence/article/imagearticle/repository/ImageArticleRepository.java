package com.reminiscence.article.imagearticle.repository;

import com.reminiscence.article.domain.ImageArticle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageArticleRepository extends JpaRepository<ImageArticle, Long>, ImageArticleRepositoryCustom {

}

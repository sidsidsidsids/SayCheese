package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {

}

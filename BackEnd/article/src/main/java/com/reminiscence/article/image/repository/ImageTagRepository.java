package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageTagRepository extends JpaRepository<ImageTag, Long> {

        @Query("select i from ImageTag i where i.image.id = :imageId and i.tag.id = :tagId")
        public ImageTag findByImageIdAndTagId(Long imageId, Long tagId);

        public void deleteByImageId(Long imageId);

}

package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.ImageTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageTagRepository extends JpaRepository<ImageTag, Long> {

        public void deleteByImageId(Long imageId);

        @Query("select i.tag.name from ImageTag i where i.image.id = :imageId")
        public Optional<List<String>> findTagNameByImageId(@Param("imageId") Long imageId);


}

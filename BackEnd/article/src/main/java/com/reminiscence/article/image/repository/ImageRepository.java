package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
    @Query("delete from Image i where i.id in :ids")
    void deleteNonOwnerImage(List<Long> ids);
}

package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.ImageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageOwnerRepository extends JpaRepository<ImageOwner, Long> {
    @Query("select i from ImageOwner i where i.image.id = :imageId and i.member.id = :memberId")
    public ImageOwner findByImageIdAndOwnerId(Long imageId, Long memberId);

}

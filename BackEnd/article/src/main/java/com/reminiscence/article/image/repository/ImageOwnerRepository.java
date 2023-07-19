package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.ImageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageOwnerRepository extends JpaRepository<ImageOwner, Long> {
    @Query("select i from ImageOwner i where i.image.id = :imageId and i.member.id = :memberId")
    public ImageOwner findByImageIdAndOwnerId(Long imageId, Long memberId);

    @Query("select i from ImageOwner i where i.member.id = :memberId")
    public List<ImageOwner> findByMemberId(@Param("memberId") Long memberId);

    @Query("select i from ImageOwner i where i.image.id = :imageId")
    public List<ImageOwner> findByImageId(@Param("imageId") Long imageId);

}

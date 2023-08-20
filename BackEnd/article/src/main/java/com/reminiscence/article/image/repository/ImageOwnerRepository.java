package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.ImageOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageOwnerRepository extends JpaRepository<ImageOwner, Long> {

    @Query("select i from ImageOwner i where i.image.id = :imageId and i.member.id = :memberId")
    public Optional<ImageOwner> findByImageIdAndMemberId(@Param("imageId") Long imageId, @Param("memberId") Long memberId);

    @Query("select i from ImageOwner i where i.member.id = :memberId")
    public Optional<List<ImageOwner>> findByMemberId(@Param("memberId") Long memberId);

    @Query("select i from ImageOwner i where i.image.id = :imageId")
    public Optional<List<ImageOwner>> findByImageId(@Param("imageId") Long imageId);

    @Modifying
    @Query("delete from ImageOwner i where i.image.id = :imageId and i.member.id = :memberId")
    public void deleteByImageIdAndMemberId(@Param("imageId") Long imageId, @Param("memberId") Long memberId);

}

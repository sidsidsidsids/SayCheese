package com.reminiscence.article.image;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageOwner;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.member.repository.MemberRepository;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
public class ImageJpaTest {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ImageOwnerRepository imageOwnerRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("소유한 이미지 조회")
    public void OwnerImageReadTest() {
        // given
        Member member = memberRepository.findById(2L).orElse(null);
        assertEquals(member.getId(), 2L);

        // when
        List<ImageOwner> findImageOwners = imageOwnerRepository.findByMemberId(member.getId());

        // then
        assertEquals(findImageOwners.get(0).getImage().getLink(), "www.daum.com");
        assertEquals(findImageOwners.get(1).getImage().getLink(), "www.google.com");
    }
    @Test
    @DisplayName("소유한 이미지가 없을 때 조회")
    public void OwnerNoneImageReadTest() {
        // given
        Member member = memberRepository.findById(3L).orElse(null);
        assertEquals(member.getId(), 3L);

        // when
        List<ImageOwner> findImageOwners = imageOwnerRepository.findByMemberId(member.getId());
        // then

        assertEquals(findImageOwners.size(), 0);
    }

    @Test
    @DisplayName("이미지 소유자 정보 조회")
    public void ImageOwnerReadTest(){
        // given
        Long imageId = 3L;
        Image image = imageRepository.findById(imageId).orElse(null);
        assertEquals(image.getId(), imageId);


        // when
        List<ImageOwner> findImages = imageOwnerRepository.findByImageId(image.getId());

        // then
        assertEquals(findImages.size(), 2);
        assertEquals(findImages.get(0).getMember().getNickname(), "se6816");
        assertEquals(findImages.get(1).getMember().getNickname(), "se6815");
    }

    @Test
    @DisplayName("이미지 삭제")
    public void ImageDeleteTest(){
        // given
        Member member = memberRepository.findById(1L).orElse(null);
        Image image = imageRepository.findById(3L).orElse(null);

        assertEquals(member.getId(), 1L);
        assertEquals(image.getId(), 3L);

        // when
        imageOwnerRepository.deleteByImageIdAndMemberId(image.getId(), member.getId());

        // then
        assertEquals(imageOwnerRepository.findByImageIdAndMemberId(image.getId(), member.getId()), null);
    }
}

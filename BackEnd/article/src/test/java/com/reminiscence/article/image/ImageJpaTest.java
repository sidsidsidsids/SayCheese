package com.reminiscence.article.image;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageOwner;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.ImageTagRepository;
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
    @Autowired
    ImageTagRepository imageTagRepository;

    @Test
    @DisplayName("소유한 이미지 조회")
    public void OwnerImageReadTest() {
        // given
        Long memberId = 2L;
        Member member = memberRepository.findById(memberId).orElse(null);
        assertNotNull(member);
        assertEquals(memberId, member.getId());

        // when
        List<ImageOwner> findImageOwners = imageOwnerRepository.findByMemberId(member.getId()).orElse(null);

        // then
        assertNotNull(findImageOwners);
        assertEquals(9, findImageOwners.size());
        for(ImageOwner imageOwner : findImageOwners){
            assertEquals(memberId, imageOwner.getMember().getId());
        }
    }
    @Test
    @DisplayName("소유한 이미지가 없을 때 조회")
    public void OwnerNoneImageReadTest() {
        // given
        Long memberId = 4L;
        Member member = memberRepository.findById(memberId).orElse(null);
        assertNotNull(member);
        assertEquals(member.getId(), memberId);

        // when
        List<ImageOwner> findImageOwners = imageOwnerRepository.findByMemberId(member.getId()).orElse(null);
        // then
        assertNotNull(findImageOwners);
        assertEquals(0, findImageOwners.size());
    }

    @Test
    @DisplayName("이미지 소유자 정보 조회")
    public void ImageOwnerReadTest(){
        // given
        Long imageId = 3L;
        Image image = imageRepository.findById(imageId).orElse(null);
        assertNotNull(image);
        assertEquals(image.getId(), imageId);


        // when
        List<ImageOwner> findImages = imageOwnerRepository.findByImageId(image.getId()).orElse(null);

        // then
        assertNotNull(findImages);
        assertEquals(2, findImages.size());
        assertEquals("se6816", findImages.get(0).getMember().getNickname());
        assertEquals("se6815", findImages.get(1).getMember().getNickname() );
    }

    @Test
    @DisplayName("이미지 삭제")
    public void ImageDeleteTest(){
        // given
        Long memberId = 1L;
        Long imageId = 3L;
        Member member = memberRepository.findById(memberId).orElse(null);
        Image image = imageRepository.findById(imageId).orElse(null);

        assertNotNull(member);
        assertNotNull(image);
        assertEquals(member.getId(), memberId);
        assertEquals(image.getId(), imageId);

        // when
        imageOwnerRepository.deleteByImageIdAndMemberId(image.getId(), member.getId());

        // then
        ImageOwner findImageOwner = imageOwnerRepository.findByImageIdAndMemberId(image.getId(), member.getId()).orElse(null);
        assertNull(findImageOwner);
    }

    @Test
    @DisplayName("이미지 태그 조회")
    public void ImageTagReadTest(){
        // given
        Long imageId = 1L;

        // when
        List<String> findTags = imageTagRepository.findTagNameByImageId(imageId).orElse(null);

        // then
        assertNotNull(findTags);
        assertEquals(4, findTags.size());
    }
}

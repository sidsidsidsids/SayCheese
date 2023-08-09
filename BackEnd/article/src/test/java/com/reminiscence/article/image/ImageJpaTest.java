package com.reminiscence.article.image;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageOwner;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.image.dto.RandomTagResponseDto;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.ImageTagRepository;
import com.reminiscence.article.image.repository.TagRepository;
import com.reminiscence.article.image.vo.ImageVo;
import com.reminiscence.article.member.repository.MemberRepository;
import static org.junit.jupiter.api.Assertions.*;

import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
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
    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("소유한 이미지 조회 테스트")
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
    @DisplayName("소유한 이미지 최신순 이미지 조회 테스트")
    public void RecentOwnerImageTest(){
        // given
        Long memberId = 1L;
        int PAGE = 1;
        PageRequest page = PageRequest.of(PAGE, Pagination.DEFAULT_OWN_IMAGE_PER_PAGE_SIZE, Sort.Direction.DESC, "createdDate");

        // when
        Page<ImageVo> images = imageRepository.findRecentOwnerImage(memberId, page).orElse(null);

        // then
        assertNotNull(images);
        assertEquals(6, images.getTotalElements());
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
    @DisplayName("이미지 소유자 정보 조회 테스트")
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
    @DisplayName("소유자 없는 이미지 조회 테스트")
    public void ImageOwnerCntTest(){
        // given

        // when
        List<ImageDeleteResponseDto> images = imageRepository.findNonOwnerImage().orElse(null);

        // then
        assertNotNull(images);
        assertEquals(1, images.size());
        assertEquals("link16", images.get(0).getImageName());
        assertEquals("png", images.get(0).getImageType());
    }

    @Test
    @DisplayName("이미지 삭제 테스트")
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
    @DisplayName("이미지 저장 테스트")
    public void ImageWriteTest(){
        // given
        String imageLink = "https://s3.ap-northeast-2.amazonaws.com/reminiscence-bucket/image/2021/04/16/1618560000_1.jpg";
        String imageType = imageLink.substring(imageLink.lastIndexOf(".")+1);
        String fileName = "1618560000_1";
        List<Long> tags = new ArrayList<>();
        tags.add(1L);
        tags.add(2L);
        tags.add(3L);
        tags.add(4L);
        String roomCode = "1234";
        Image image = Image.builder()
                .link(imageLink)
                .type(imageType)
                .name(fileName)
                .build();
        // when
        imageRepository.save(image);
        Image findImage = imageRepository.findById(20L).orElse(null);

        // then
        assertNotNull(findImage);
        assertEquals(imageLink, findImage.getLink());
        assertEquals(imageType, findImage.getType());
        assertEquals(fileName, findImage.getName());
    }

    @Test
    @DisplayName("이미지 태그 조회 테스트")
    public void ImageTagReadTest(){
        // given
        Long imageId = 1L;

        // when
        List<String> findTags = imageTagRepository.findTagNameByImageId(imageId).orElse(null);

        // then
        assertNotNull(findTags);
        assertEquals(4, findTags.size());
    }
    @Test
    @DisplayName("랜덤 태그 조회 테스트")
    public void RandomTagReadTest(){
        // given

        // when
        List<RandomTagResponseDto> findTags = tagRepository.findRandomTags().orElse(null);

        // then
        assertNotNull(findTags);
        assertEquals(4, findTags.size());
    }
}

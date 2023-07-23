package com.reminiscence.article.ImageArticle;

import com.reminiscence.article.domain.*;
import com.reminiscence.article.image.repository.ImageTagRepository;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.TagRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import com.reminiscence.article.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


@DataJpaTest
public class ImageArticleJpaTest{
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageArticleRepository imageArticleRepository;
    @Autowired
    private LoverRepository loverRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ImageTagRepository imageTagRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private ImageOwnerRepository imageOwnerRepository;

    @Test
    @DisplayName("좋아요순 이미지 게시글 목록 조회 테스트")
    public void getHotImageArticleListTest(){
        //given
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        //when
        List<ImageArticleListResponseDto> hotImageArticles = imageArticleRepository.findCommonImageArticles(page).orElse(null);
        //then
        assertNotNull(hotImageArticles);
        assertEquals(10, hotImageArticles.size());
        assertEquals("www.google.com", hotImageArticles.get(0).getImageLink());
        assertEquals(3, hotImageArticles.get(0).getLoverCnt());
        assertEquals("se6816", hotImageArticles.get(0).getAuthor());
    }

    @Test
    @DisplayName("최근 작성된 이미지 게시글 목록 조회 테스트")
    public void getRecentImageArticleTest() {
        //given
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        //when
        List<ImageArticleListResponseDto> recentImageArticle = imageArticleRepository.findCommonImageArticles(page).orElse(null);
        //then
        assertNotNull(recentImageArticle);
        assertEquals(10, recentImageArticle.size());
        assertEquals("link15", recentImageArticle.get(0).getImageLink());
        assertEquals(0, recentImageArticle.get(0).getLoverCnt());
        assertEquals("se6815", recentImageArticle.get(0).getAuthor());
    }
    @Test
    @DisplayName("태그 관련 이미지 게시글 조회 테스트")
    public void getTagImageArticleTest(){
        //given
        Long tagId = 1L;
        Tag tag = tagRepository.findById(tagId).orElse(null);
        assertNotNull(tag, "잘못된 태그가 들어왔습니다.");
        assertEquals(tagId, tag.getId());

        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        //when
        List<ImageArticleListResponseDto> tagImageArticle = imageArticleRepository.findTagImageArticles(tagId, page).orElse(null);

        //then
        assertNotNull(tagImageArticle);
        assertEquals(1, tagImageArticle.size());
        assertEquals("www.naver.com", tagImageArticle.get(0).getImageLink());
        assertEquals(1, tagImageArticle.get(0).getLoverCnt());
        assertEquals("se6816", tagImageArticle.get(0).getAuthor());
    }

    @Test
    @DisplayName("태그관련 랜덤 이미지 게시글 목록 조회 테스트")
    public void getTagRandomImageArticleTest(){
        //given
        Tag randomTag = tagRepository.findRandomTag().orElse(null);
        assertNotNull(randomTag, "잘못된 태그가 들어왔습니다.");
        System.out.println("randomTag: " + randomTag.getName());

        Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "random");

        //when
        List<ImageArticleListResponseDto> randomTagImageArticles = imageArticleRepository.findTagImageArticles(randomTag.getId(), page).orElse(null);
        //then
        assertNotNull(randomTagImageArticles);
        assertEquals(1, randomTagImageArticles.size());
        assertEquals("www.naver.com", randomTagImageArticles.get(0).getImageLink());
        assertEquals(1, randomTagImageArticles.get(0).getLoverCnt());
        assertEquals("se6816", randomTagImageArticles.get(0).getAuthor());
    }

    @Test
    @DisplayName("무작위 이미지 게시글 목록 조회 테스트")
    public void getRandomImageArticleListTest(){
        //given
        PageRequest page = PageRequest.of(0, 5, Sort.Direction.DESC, "random");

        //when
        List<ImageArticleListResponseDto> randomImageArticles = imageArticleRepository.findCommonImageArticles(page).orElse(null);

        //then
        assertNotNull(randomImageArticles);
        assertEquals(5, randomImageArticles.size());
        for(ImageArticleListResponseDto imageArticle : randomImageArticles){
            System.out.println(imageArticle.toString());
        }
    }

    @Test
    @DisplayName("이미지 게시글 상세조회 테스트")
    public void getImageArticleTest(){
        //given
        Long articleId = 50L;

        //when
        ImageArticleDetailResponseDto imageArticle = imageArticleRepository.findImageArticleDetailById(articleId).orElse(null);
        assertNotNull(imageArticle);

        List<String> findTags = imageTagRepository.findTagNameByImageId(imageArticle.getImageId()).orElse(null);
        assertNotNull(findTags);
        imageArticle.setTags(findTags);

        //then
        assertEquals("www.naver.com",imageArticle.getImgLink());
        assertEquals(1L, imageArticle.getMemberId());
        assertEquals(4, findTags.size());
        assertEquals("se6816",imageArticle.getName());
        assertEquals(0,imageArticle.getLoverYn());
        assertEquals(1,imageArticle.getLoverCnt());
}


    @Test
    @DisplayName("이미지 게시글 작성 테스트")
    public void createImageArticleTest(){
        //given

        Long memberId = 1L;
        Long imageId = 19L;

        Member member = memberRepository.findById(memberId).orElse(null);
        assertNotNull(member);
        assertEquals(member.getId(), memberId);

        Image image = imageRepository.findById(imageId).orElse(null);
        assertNotNull(image);
        assertEquals(image.getId(), imageId);

        //when
        ImageArticle imageArticle = ImageArticle.builder()
                .member(member)
                .image(image)
                .build();
        imageArticleRepository.save(imageArticle);

        List<ImageArticle> all = imageArticleRepository.findAll();
        ImageArticle findImageArticle = all.get(all.size() - 1);


        //then
        assertEquals(findImageArticle.getMember().getId(), memberId);
        assertEquals(findImageArticle.getImage().getId(), imageId);
        assertEquals(findImageArticle.getImage().getLink(), "link16");
    }

    @Test
    @DisplayName("이미지 게시글만 삭제 테스트")
    public void deleteOnlyImageArticleTest(){
        //given
        Long articleId = 50L;
        ImageArticle imageArticle = imageArticleRepository.findImageArticleOfMemberById(articleId).orElse(null);

        assertNotNull(imageArticle, "이미지 게시글이 존재하지 않습니다.");

        //when
        loverRepository.deleteByArticleId(articleId);
        imageArticleRepository.delete(imageArticle);

        //then
        List<Lover> lovers = loverRepository.findByArticleId(articleId).orElse(null);
        List<ImageArticle> findArticles = imageArticleRepository.findAll();

        assertNotNull(lovers);
        assertNotNull(findArticles);
        assertEquals(0, lovers.size());
        assertEquals(17, findArticles.size());

    }

    @Test
    @DisplayName("이미지 게시글 삭제 테스트")
    public void deleteImageArticleTest(){
        //given
        Long articleId = 50L;
        ImageArticle imageArticle = imageArticleRepository.findImageArticleOfAllById(articleId).orElse(null);

        assertNotNull(imageArticle, "이미지 게시글이 존재하지 않습니다.");

        //when
        loverRepository.deleteByArticleId(articleId);
        imageArticleRepository.delete(imageArticle);
        imageOwnerRepository.deleteByImageIdAndMemberId(imageArticle.getImage().getId(), imageArticle.getMember().getId());


        //then
        List<Lover> lovers = loverRepository.findByArticleId(articleId).orElse(null);
        List<ImageArticle> findArticles = imageArticleRepository.findAll();

        assertEquals(0, lovers.size());
        assertEquals(17, findArticles.size());

    }
    @Test
    @DisplayName("랜덤한 태그 조회 테스트")
    public void getRandomTagsTest(){
        //given

        //when
        Tag randomTag = tagRepository.findRandomTag().orElse(null);

        //then
        assertNotNull(randomTag);
        System.out.println(randomTag.getName());
    }
}

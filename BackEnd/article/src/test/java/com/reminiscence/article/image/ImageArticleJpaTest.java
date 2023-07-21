package com.reminiscence.article.image;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.TagRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import com.reminiscence.article.member.repository.MemberRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;


@DataJpaTest
public class ImageArticleJpaTest{
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageArticleRepository imageArticleRepository;
    @Autowired
    private ImageOwnerRepository imageOwnerRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private LoverRepository loverRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    @DisplayName("이미지 게시글 조회 테스트")
    public void getImageArticleTest(){
        //given
        Long articleId = 50L;

        //when

        ImageArticle imageArticle = queryFactory.selectFrom(QImageArticle.imageArticle)
                .join(QImageArticle.imageArticle.member, QMember.member).fetchJoin()
                .join(QImageArticle.imageArticle.image, QImage.image).fetchJoin()
                .join(QImage.image.imageTags, QImageTag.imageTag).fetchJoin()
                .join(QImageTag.imageTag.tag, QTag.tag).fetchJoin()
                .where(QImageArticle.imageArticle.id.eq(articleId))
                .fetchOne();


        //then
        assertEquals(imageArticle.getId(), articleId);
        assertEquals(imageArticle.getImage().getLink(), "www.naver.com");
        assertEquals(imageArticle.getMember().getId(), 1L);
        assertEquals(1,imageArticle.getImage().getImageTags().get(0).getTag().getId());
        assertEquals( 2,imageArticle.getImage().getImageTags().get(1).getTag().getId());
        assertEquals( 3,imageArticle.getImage().getImageTags().get(2).getTag().getId());
        assertEquals( 4,imageArticle.getImage().getImageTags().get(3).getTag().getId());
        assertEquals("se6816",imageArticle.getMember().getNickname());
        assertEquals(0,imageArticle.getMember().getLovers().size());
        assertEquals(1,imageArticle.getLovers().size());
}
    @Test
    @DisplayName("이미지 게시글 작성 테스트")
    public void createImageArticleTest(){
        //given

        Long memberId = 1L;
        Long imageId = 3L;

        Member member = memberRepository.findById(memberId).orElse(null);
        assertEquals(member.getId(), memberId);

        Image image = imageRepository.findById(imageId).orElse(null);
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
        assertEquals(findImageArticle.getImage().getLink(), "www.google.com");
    }

    @Test
    @DisplayName("이미지 게시글만 삭제 테스트")
    public void deleteOnlyImageArticleTest(){
        //given
        Long articleId = 50L;
        ImageArticle imageArticle = queryFactory.selectFrom(QImageArticle.imageArticle)
                .innerJoin(QImageArticle.imageArticle.member).fetchJoin()
                .where(QImageArticle.imageArticle.id.eq(articleId))
                .fetchOne();
        assertNotNull(imageArticle, "이미지 게시글이 존재하지 않습니다.");

        //when
        loverRepository.deleteByArticleId(articleId);
        imageArticleRepository.delete(imageArticle);

        //then
        List<Member> findMembers = memberRepository.findAll();
        List<ImageArticle> findArticles = imageArticleRepository.findAll();

        assertEquals(findMembers.size(), 3);
        assertEquals(findArticles.size(), 2);

    }
}

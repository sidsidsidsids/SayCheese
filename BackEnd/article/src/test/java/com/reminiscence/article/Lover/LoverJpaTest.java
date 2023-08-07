package com.reminiscence.article.Lover;

import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.Lover;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class LoverJpaTest {
    @Autowired
    LoverRepository loverRepository;
    @Autowired
    ImageArticleRepository imageArticleRepository;

    @Test
    @DisplayName("좋아요 삽입 테스트")
    public void insertLoverTest(){
        //given
        Long memberId = 1L;
        Long articleId = 58L;

        //when
        ImageArticle findImageArticle = imageArticleRepository.findById(articleId).orElse(null);
        assertNotNull(findImageArticle);
        assertEquals(articleId, findImageArticle.getId());
        Lover lover = Lover.builder()
                .memberId(memberId)
                .article(findImageArticle)
                .build();
        loverRepository.save(lover);
        Lover findLover = loverRepository.findByMemberIdAndArticleId(memberId, articleId).orElse(null);

        //then
        assertNotNull(findLover);
        assertEquals(memberId, findLover.getMemberId());
        assertEquals(articleId, findLover.getArticle().getId());
    }
    @Test
    @DisplayName("좋아요 취소 테스트")
    public void deleteLoverTest(){
        //given
        Long memberId = 1L;
        Long articleId = 55L;

        //when
        Lover findLover = loverRepository.findByMemberIdAndArticleId(memberId, articleId).orElse(null);
        assertNotNull(findLover);
        assertEquals(memberId, findLover.getMemberId());
        assertEquals(articleId, findLover.getArticle().getId());

        loverRepository.delete(findLover);
        //then
        Lover deleteLover = loverRepository.findByMemberIdAndArticleId(memberId, articleId).orElse(null);
        assertNull(deleteLover);
    }
}

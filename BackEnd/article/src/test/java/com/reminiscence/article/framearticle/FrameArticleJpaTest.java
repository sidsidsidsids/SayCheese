package com.reminiscence.article.framearticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;
import com.reminiscence.article.framearticle.dummy.DummyFrameArticleRequestDto;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class FrameArticleJpaTest {

    @Autowired
    private FrameArticleRepository frameArticleRepository;
    @Autowired
    private FrameRepository frameRepository;

    @Autowired
    private MemberRepository memberRepository;

    private ObjectMapper objectMapper=new ObjectMapper();
    @Test
    @DisplayName("프레임 저장 테스트")
    public void writeFrameArticleTest() throws JsonProcessingException {
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto=new DummyFrameArticleRequestDto.Builder()
                .name("test")
                .subject("test")
                .link("http://naver.com")
                .isPublic(true)
                .frameSpecification("A")
                .build();
        FrameArticleRequestDto frameArticleRequestDto=objectMapper.readValue(objectMapper.writeValueAsString(dummyFrameArticleRequestDto),FrameArticleRequestDto.class);


        FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto=FrameArticleAndMemberRequestDto.builder()
                .frameArticleRequestDto(frameArticleRequestDto)
                .member(memberRepository.findById(2L).orElse(null))
                .build();
        Frame frame= FrameArticleAndMemberRequestDto.toEntity(frameArticleAndMemberRequestDto.getFrameArticleRequestDto());
        frameRepository.save(frame);
        FrameArticle frameArticle=FrameArticle.builder()
                .frame(frame)
                .member(frameArticleAndMemberRequestDto.getMember())
                .subject(frameArticleAndMemberRequestDto.getFrameArticleRequestDto().getSubject())
                .build();

        frameArticleRepository.save(frameArticle);

        Assertions.assertThat(frameRepository.findById(frame.getId()).orElse(new Frame()).getId()).isEqualTo(frame.getId());
        Assertions.assertThat(frameArticleRepository.findById(frameArticle.getId()).orElse(new FrameArticle()).getId()).isEqualTo(frameArticle.getId());

    }
}

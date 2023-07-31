package com.reminiscence.article.framearticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;
import com.reminiscence.article.framearticle.dummy.DummyFrameArticleRequestDto;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
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

    @Autowired
    private LoverRepository loverRepository;
    private ObjectMapper objectMapper=new ObjectMapper();
    @Test
    @DisplayName("프레임 저장 테스트")
    public void writeFrameArticleTest() throws JsonProcessingException {
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto=new DummyFrameArticleRequestDto.Builder()
                .name("test")
                .subject("test")
                .link("http://special.com/image/21241test.jpg")
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
        Assertions.assertThat(frameRepository.findById(frame.getId()).orElse(new Frame()).getType()).isEqualTo("jpg");
        Assertions.assertThat(frameRepository.findById(frame.getId()).orElse(new Frame()).getId()).isEqualTo(frame.getId());
        Assertions.assertThat(frameArticleRepository.findById(frameArticle.getId()).orElse(new FrameArticle()).getId()).isEqualTo(frameArticle.getId());

    }


    @Test
    @DisplayName("프레임 삭제 테스트")
    public void deleteFrameArticleTest() throws Exception {
        Long ownerMemberId=2L;
        Long anotherMemberId=3L;
        Assertions.assertThat(frameArticleRepository.findById(68L).orElse(null)).isNotNull();
        FrameArticle frameArticle=frameArticleRepository.findById(68L).orElseThrow(
                ()->new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));
        if(frameArticle.getMember().getId()!=ownerMemberId){
            throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_DELETE_AUTH);
        }
        loverRepository.deleteByArticleId(frameArticle.getId());

        frameArticleRepository.delete(frameArticle);
        Assertions.assertThat(frameArticleRepository.findById(68L).orElse(null)).isNull();
    }
    @Test
    @DisplayName("프레임 삭제 테스트(소유자가 아닌 자가 삭제 시도를 할 때")
    public void deleteFrameArticleNoAuthFailTest() throws Exception {
        Long ownerMemberId=2L;
        Long anotherMemberId=3L;
        Assertions.assertThatThrownBy(()->{
            Assertions.assertThat(frameArticleRepository.findById(68L).orElse(null)).isNotNull();
            FrameArticle frameArticle=frameArticleRepository.findById(68L).orElseThrow(
                    ()->new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));
            if(frameArticle.getMember().getId()!=anotherMemberId){
                throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_DELETE_AUTH);
            }
            loverRepository.deleteByArticleId(frameArticle.getId());

            frameArticleRepository.delete(frameArticle);
            Assertions.assertThat(frameArticleRepository.findById(68L).orElse(null)).isNull();
        }).isInstanceOf(FrameArticleException.class);

    }
    @Test
    @DisplayName("프레임 삭제 테스트(존재하지 않는 글을 삭제 시도를 할 때")
    public void deleteFrameArticleNoArticleIdFailTest() throws Exception {
        Long ownerMemberId=2L;
        Long anotherMemberId=3L;
        Assertions.assertThatThrownBy(()->{
             FrameArticle frameArticle=frameArticleRepository.findById(680000L).orElseThrow(
                    ()->new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_DATA));
            if(frameArticle.getMember().getId()!=ownerMemberId){
                throw new FrameArticleException(FrameArticleExceptionMessage.INVALID_DELETE_AUTH);
            }
            loverRepository.deleteByArticleId(frameArticle.getId());

            frameArticleRepository.delete(frameArticle);
        }).isInstanceOf(FrameArticleException.class);

    }
}

package com.reminiscence.article.framearticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameArticle;
import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto = DummyFrameArticleRequestDto.builder()
                .name("test.jpg")
                .subject("test")
                .fileType("frame")
                .isPublic(true)
                .frameSpecification("vertical")
                .build();

        FrameArticleRequestDto frameArticleRequestDto=objectMapper.readValue(objectMapper.writeValueAsString(dummyFrameArticleRequestDto),FrameArticleRequestDto.class);
        String link = "https://test.s3.region.amazonaws.com/test/test.jpg";
        FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto=FrameArticleAndMemberRequestDto.builder()
                .frameArticleRequestDto(frameArticleRequestDto)
                .member(memberRepository.findById(2L).orElse(null))
                .build();
        Frame frame= FrameArticleAndMemberRequestDto.toEntity(frameArticleAndMemberRequestDto.getFrameArticleRequestDto(), link);
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


    @Test
    @DisplayName("좋아요순 프레임 게시글 목록 조회 테스트(회원)")
    public void getMemberHotFrameArticleListTest(){
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");

        int page=pageable.getPageNumber();
        if(page<=0){
            page=1;
        }

        Long memberId = 1L;
        String searchWord = "se";
        String frameSpec = "";

        //when
        Page<FrameArticleVo> hotFrameArticles = frameArticleRepository.findMemberFrameArticles(pageable, memberId, searchWord, frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(page, hotFrameArticles.getTotalPages(), hotFrameArticles.getTotalElements());

        for(FrameArticleVo frameArticleVo:hotFrameArticles.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertNotNull(hotFrameArticles);
        assertEquals(1, frameArticleListResponseDto.getPageNavigator().getCurPage());
        assertEquals(2, frameArticleListResponseDto.getPageNavigator().getTotalPages());
        assertEquals(12, frameArticleListResponseDto.getPageNavigator().getTotalDataCount());
        assertEquals(3, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverCnt());
        assertEquals("se6815", frameArticleListResponseDto.getFrameArticleVoList().get(0).getAuthor());
        assertEquals(1L, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverYn());
    }
    @Test
    @DisplayName("좋아요순 프레임 게시글 목록 조회 테스트(비회원)")
    public void getNonMemberHotFrameArticleListTest(){
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        String searchWord = "";
        String frameSpec = "";
        //when
        Page<FrameArticleVo> hotFrameArticles = frameArticleRepository.findNonMemberFrameArticles(pageable, searchWord, frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), hotFrameArticles.getTotalPages(), hotFrameArticles.getTotalElements());

        for(FrameArticleVo frameArticleVo:hotFrameArticles.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertNotNull(hotFrameArticles);
        assertEquals(1, frameArticleListResponseDto.getPageNavigator().getCurPage());
        assertEquals(2, frameArticleListResponseDto.getPageNavigator().getTotalPages());
        assertEquals(12, frameArticleListResponseDto.getPageNavigator().getTotalDataCount());
        assertEquals(3, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverCnt());
        assertEquals("se6815", frameArticleListResponseDto.getFrameArticleVoList().get(0).getAuthor());
        assertEquals(0L, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverYn());

        pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "lover");
        //when
        hotFrameArticles = frameArticleRepository.findNonMemberFrameArticles(pageable, searchWord, frameSpec);

        frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), hotFrameArticles.getTotalPages(), hotFrameArticles.getContent().size());

        for(FrameArticleVo frameArticleVo:hotFrameArticles.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertNotNull(hotFrameArticles);
        assertEquals(1, frameArticleListResponseDto.getPageNavigator().getCurPage());
        assertEquals(3, frameArticleListResponseDto.getPageNavigator().getTotalPages());
        assertEquals(5, frameArticleListResponseDto.getPageNavigator().getTotalDataCount());
        assertEquals(3, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverCnt());
        assertEquals("se6815", frameArticleListResponseDto.getFrameArticleVoList().get(0).getAuthor());
        assertEquals(0L, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverYn());



    }

    @Test
    @DisplayName("최근 작성된 프레임 게시글 목록 조회 테스트(회원)")
    public void getMemberRecentFrameArticleTest() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        Long memberId = 1L;
        String searchWord = "";
        String frameSpec = "";
        //when
        Page<FrameArticleVo> recentFrameArticle = frameArticleRepository.findMemberFrameArticles(pageable,memberId,searchWord, frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), recentFrameArticle.getTotalPages(), recentFrameArticle.getTotalElements());

        for(FrameArticleVo frameArticleVo:recentFrameArticle.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertEquals(1, frameArticleListResponseDto.getPageNavigator().getCurPage());
        assertEquals(2, frameArticleListResponseDto.getPageNavigator().getTotalPages());
        assertEquals(12, frameArticleListResponseDto.getPageNavigator().getTotalDataCount());
        assertEquals("https://png.pngtree.com/png-vector/20210324/ourmid/pngtree-picture-frame-png-image_3135232.jpg", frameArticleListResponseDto.getFrameArticleVoList().get(0).getFrameLink());
        assertEquals(1, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverCnt());
        assertEquals("se6817", frameArticleListResponseDto.getFrameArticleVoList().get(0).getAuthor());
        assertEquals(1L, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverYn());
    }


    @Test
    @DisplayName("최근 작성된 프레임 게시글 목록 조회 테스트(비회원)")
    public void getNonMemberRecentFrameArticleTest() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        String searchWord = "";
        String frameSpec = "";

        //when
        Page<FrameArticleVo> recentFrameArticle = frameArticleRepository.findNonMemberFrameArticles(pageable,searchWord, frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), recentFrameArticle.getTotalPages(), recentFrameArticle.getTotalElements());

        for(FrameArticleVo frameArticleVo:recentFrameArticle.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertEquals(1, frameArticleListResponseDto.getPageNavigator().getCurPage());
        assertEquals(2, frameArticleListResponseDto.getPageNavigator().getTotalPages());
        assertEquals(12, frameArticleListResponseDto.getPageNavigator().getTotalDataCount());
        assertEquals("https://png.pngtree.com/png-vector/20210324/ourmid/pngtree-picture-frame-png-image_3135232.jpg", frameArticleListResponseDto.getFrameArticleVoList().get(0).getFrameLink());
        assertEquals(1, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverCnt());
        assertEquals("se6817", frameArticleListResponseDto.getFrameArticleVoList().get(0).getAuthor());
        assertEquals(0L, frameArticleListResponseDto.getFrameArticleVoList().get(0).getLoverYn());
    }

    @Test
    @DisplayName("무작위 프레임 게시글 목록 조회 테스트(회원)")
    public void getMemberRandomFrameArticleListTest(){
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "random");
        String searchWord = "";
        String frameSpec = "";

        //when
        Page<FrameArticleVo> randomFrameArticles = frameArticleRepository.findMemberFrameArticles(pageable,memberId,searchWord, frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), randomFrameArticles.getTotalPages(), randomFrameArticles.getTotalElements());

        for(FrameArticleVo frameArticleVo:randomFrameArticles.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertEquals(5, frameArticleListResponseDto.getFrameArticleVoList().size());
        for(FrameArticleVo frameArticleVo : frameArticleListResponseDto.getFrameArticleVoList()){
            System.out.println(frameArticleVo.toString());
        }
    }

    @Test
    @DisplayName("무작위 프레임 게시글 목록 조회 테스트(비회원)")
    public void getNonMemberRandomFrameArticleListTest(){
        //given
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC, "random");
        String searchWord = "";
        String frameSpec = "";

        //when
        Page<FrameArticleVo> randomFrameArticles = frameArticleRepository.findNonMemberFrameArticles(pageable,searchWord,frameSpec);
        FrameArticleListResponseDto frameArticleListResponseDto = new FrameArticleListResponseDto(pageable.getPageNumber(), randomFrameArticles.getTotalPages(), randomFrameArticles.getTotalElements());

        for(FrameArticleVo frameArticleVo:randomFrameArticles.getContent()){
            frameArticleListResponseDto.add(new FrameArticleVo(frameArticleVo));
        }

        //then
        assertEquals(5, frameArticleListResponseDto.getFrameArticleVoList().size());
        for(FrameArticleVo frameArticle : randomFrameArticles){
            System.out.println(frameArticle.toString());
        }
    }

}

package com.reminiscence.article.notice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.member.repository.MemberRepository;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleListResponseDto;
import com.reminiscence.article.notice.repository.NoticeRepository;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class NoticeJpaTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    NoticeRepository noticeRepository;



    private Member member;
    private ObjectMapper objectMapper=new ObjectMapper();

    @BeforeEach
    public void init() {
        member=memberRepository.findById(1L).orElse(null);

    }

    @Test
    @DisplayName("공지 작성 테스트")
    public void noticeArticleWriteTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 10시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();

        NoticeArticleRequestDto noticeArticleRequestDto=objectMapper.readValue(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto),NoticeArticleRequestDto.class);
        NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto=NoticeArticleAndMemberRequestDto.builder()
                .noticeArticleRequestDto(noticeArticleRequestDto)
                .member(member)
                .build();
        NoticeArticle noticeArticle=noticeArticleAndMemberRequestDto.toEntity(noticeArticleAndMemberRequestDto);
        noticeRepository.save(noticeArticle);

        Assertions.assertThat(noticeRepository.findById(noticeArticle.getId()).orElse(null).getId()).isEqualTo(noticeArticle.getId());
    }
    @Test
    @DisplayName("공지 수정 테스트")
    public void noticeArticleModifyTest() throws Exception {
        // 공지 생성
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 10시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();

        NoticeArticleRequestDto noticeArticleRequestDto=objectMapper.readValue(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto),NoticeArticleRequestDto.class);
        NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto=NoticeArticleAndMemberRequestDto.builder()
                .noticeArticleRequestDto(noticeArticleRequestDto)
                .member(member)
                .build();
        NoticeArticle noticeArticle=noticeArticleAndMemberRequestDto.toEntity(noticeArticleAndMemberRequestDto);
        noticeRepository.save(noticeArticle);

        Assertions.assertThat(noticeRepository.findById(noticeArticle.getId()).orElse(null).getId()).isEqualTo(noticeArticle.getId());


        // 공지 수정 시도
        NoticeArticle newNoticeArticle=noticeRepository.findById(noticeArticle.getId()).orElse(null);

        DummyNoticeArticleRequestDto.Builder modifyBuilder=new DummyNoticeArticleRequestDto.Builder();
        modifyBuilder.subject("시스템 점검 안내")
                    .content("내일 12시에 점검 있어요");
        DummyNoticeArticleRequestDto modifyDummyNoticeArticleRequestDto=modifyBuilder.build();
        NoticeArticleRequestDto modifyNoticeArticleRequestDto=objectMapper.readValue(objectMapper.writeValueAsString(modifyDummyNoticeArticleRequestDto),NoticeArticleRequestDto.class);

        newNoticeArticle.modifySubject(modifyNoticeArticleRequestDto.getSubject());
        newNoticeArticle.modifyContent(modifyNoticeArticleRequestDto.getContent());

        Assertions.assertThat(noticeRepository.findById(noticeArticle.getId()).orElse(null).getSubject()).isEqualTo(modifyNoticeArticleRequestDto.getSubject());
        Assertions.assertThat(noticeRepository.findById(noticeArticle.getId()).orElse(null).getContent()).isEqualTo(modifyNoticeArticleRequestDto.getContent());


    }

    @Test
    @DisplayName("공지 조회 테스트")
    public void noticeArticleListTest() throws Exception {
        Pageable pageable=PageRequest.of(0,10, Sort.Direction.DESC,"id");
        int page=pageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Page<NoticeArticle> noticeArticlePageList=noticeRepository.findNoticeArticle(pageable);
        NoticeArticleListResponseDto noticeArticleListResponseDto =new NoticeArticleListResponseDto();

        noticeArticleListResponseDto.setCurPage(page);
        noticeArticleListResponseDto.setTotalPages(noticeArticlePageList.getTotalPages());
        noticeArticleListResponseDto.setTotalDataCount(noticeArticlePageList.getTotalElements());

        for(NoticeArticle noticeArticle:noticeArticlePageList.getContent()){
            noticeArticleListResponseDto.add(new NoticeArticleVo(noticeArticle));
        }

        Assertions.assertThat(noticeArticleListResponseDto.getCurPage()).isEqualTo(1);
        Assertions.assertThat(noticeArticleListResponseDto.getTotalPages()).isEqualTo(5);
        Assertions.assertThat(noticeArticleListResponseDto.getTotalDataCount()).isEqualTo(49);
        Assertions.assertThat(noticeArticleListResponseDto.getNoticeArticleVoList().size()).isEqualTo(10);

    }
}

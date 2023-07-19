package com.reminiscence.article.notice;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.member.repository.MemberRepository;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.repository.NoticeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;

import java.util.List;

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
}

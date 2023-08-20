package com.reminiscence.article.notice.dto;

import com.reminiscence.article.domain.Member;
import com.reminiscence.article.domain.NoticeArticle;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeArticleAndMemberRequestDto {
    private final Member member;
    private final NoticeArticleRequestDto noticeArticleRequestDto;

    @Builder
    public NoticeArticleAndMemberRequestDto(Member member, NoticeArticleRequestDto noticeArticleRequestDto) {
        this.member = member;
        this.noticeArticleRequestDto = noticeArticleRequestDto;
    }

    public static NoticeArticle toEntity(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto) {
        return NoticeArticle.builder()
                .member(noticeArticleAndMemberRequestDto.getMember())
                .subject(noticeArticleAndMemberRequestDto.getNoticeArticleRequestDto().getSubject())
                .content(noticeArticleAndMemberRequestDto.getNoticeArticleRequestDto().getContent())
                .build();
    }

}

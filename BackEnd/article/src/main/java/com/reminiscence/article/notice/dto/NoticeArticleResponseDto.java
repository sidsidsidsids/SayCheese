package com.reminiscence.article.notice.dto;

import com.reminiscence.article.domain.NoticeArticle;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeArticleResponseDto {
    private Long id;
    private String subject;
    private String content;
    private String writer;
    private LocalDateTime createdDate;

    public NoticeArticleResponseDto(NoticeArticle noticeArticle){
        this.id=noticeArticle.getId();
        this.subject=noticeArticle.getSubject();
        this.content=noticeArticle.getContent();
        this.writer=noticeArticle.getMember().getNickname();
        this.createdDate=noticeArticle.getCreatedDate();
    }
}

package com.reminiscence.article.framearticle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class FrameArticleListResponseDto {

    private Long articleId;
    private String subject;
    private String frameLink;
    private Long loverCnt;
    private LocalDateTime createdDate;
    private String author;
    private Long loverYn;

    public FrameArticleListResponseDto(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author, Long loverYn) {
        this.articleId = articleId;
        this.subject = subject;
        this.frameLink = frameLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
        if (loverYn == null) {
            this.loverYn = 0L;
        } else {
            this.loverYn = 1L;
        }
    }

    public FrameArticleListResponseDto(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author) {
        this.articleId = articleId;
        this.subject = subject;
        this.frameLink = frameLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
        this.loverYn = 0L;
    }

    public void changeLoverYn(Long loverYn) {
        this.loverYn = loverYn;
    }
}

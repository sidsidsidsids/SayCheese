package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import com.reminiscence.article.message.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FrameArticleResponseDto {
    private Long articleId;
    private String subject;
    private String frameLink;
    private Long loverCnt;
    private LocalDateTime createdDate;
    private String author;
    private Boolean isPublic; // DB에서의 공개여부(openYn) 값을 boolean 형태로 반환
    private FrameSpecification frameSpecification;
    private Long loverYn;
    private Boolean isMine;

    public FrameArticleResponseDto(FrameArticleVo frameArticleVo) {
        this.articleId = frameArticleVo.getArticleId();
        this.subject = frameArticleVo.getSubject();
        this.frameLink = frameArticleVo.getFrameLink();
        this.loverCnt = frameArticleVo.getLoverCnt();
        this.createdDate = frameArticleVo.getCreatedDate();
        this.author = frameArticleVo.getAuthor();
        this.isPublic = frameArticleVo.getIsPublic();
        this.frameSpecification = frameArticleVo.getFrameSpecification();
        this.loverYn = frameArticleVo.getLoverYn();
        this.isMine = frameArticleVo.getIsMine();
    }

    public FrameArticleResponseDto(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author, Character openYn, FrameSpecification frameSpecification, Long loverYn, Boolean isMine) {
        boolean isPublic = openYn == 'Y';
        this.articleId = articleId;
        this.subject = subject;
        this.frameLink = frameLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
        this.isPublic = isPublic;
        this.frameSpecification = frameSpecification;
        if (loverYn == null) {
            this.loverYn = 0L;
        } else {
            this.loverYn = 1L;
        }
        this.isMine = isMine;
    }

    public FrameArticleResponseDto(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author, Character openYn, FrameSpecification frameSpecification) {
        boolean isPublic = openYn == 'Y';
        this.articleId = articleId;
        this.subject = subject;
        this.frameLink = frameLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
        this.isPublic = isPublic;
        this.frameSpecification = frameSpecification;
        this.loverYn = 0L;
        this.isMine = false;
    }

    public void changeLoverYn(Long loverYn) {
        this.loverYn = loverYn;
    }
}

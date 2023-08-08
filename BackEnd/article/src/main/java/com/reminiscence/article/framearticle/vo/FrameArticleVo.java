package com.reminiscence.article.framearticle.vo;

import com.reminiscence.article.domain.FrameSpecification;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
public class FrameArticleVo implements Serializable {

    private Long articleId;
    private String subject;
    private String frameLink;
    private Long loverCnt;
    private LocalDateTime createdDate;
    private String author;
    private Boolean isPublic; // DB에서의 공개여부(openYn) 값을 boolean 형태로 반환
    private FrameSpecification frameSpecification;
    private Long loverYn;

    public FrameArticleVo(FrameArticleVo frameArticleVo) {
        this.articleId = frameArticleVo.getArticleId();
        this.subject = frameArticleVo.getSubject();
        this.frameLink = frameArticleVo.getFrameLink();
        this.loverCnt = frameArticleVo.getLoverCnt();
        this.createdDate = frameArticleVo.getCreatedDate();
        this.author = frameArticleVo.getAuthor();
        this.isPublic = frameArticleVo.getIsPublic();
        this.frameSpecification = frameArticleVo.getFrameSpecification();
        if (loverYn == null) {
            this.loverYn = 0L;
        } else {
            this.loverYn = 1L;
        }
    }

    public FrameArticleVo(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author, Character openYn, FrameSpecification frameSpecification, Long loverYn) {
        Boolean isPublic = (openYn=='Y') ? true:false;
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
    }

    public FrameArticleVo(Long articleId, String subject, String frameLink, Long loverCnt, LocalDateTime createdDate, String author, Character openYn, FrameSpecification frameSpecification) {
        Boolean isPublic = (openYn=='Y') ? true:false;
        this.articleId = articleId;
        this.subject = subject;
        this.frameLink = frameLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
        this.isPublic = isPublic;
        this.frameSpecification = frameSpecification;
        this.loverYn = 0L;
    }

    public void changeLoverYn(Long loverYn) {
        this.loverYn = loverYn;
    }
}

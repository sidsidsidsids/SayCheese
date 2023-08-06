package com.reminiscence.article.imagearticle.dto;

import com.reminiscence.article.domain.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageArticleDetailResponseDto {

    private Long memberId;
    private Long imageId;
    private String author;
    private LocalDateTime createdDate;
    private String imgLink;
    private Long loverCnt;
    private Long loverYn;
    private List<String> tags = new ArrayList<>();

    public ImageArticleDetailResponseDto(Long memberId, Long imageId, String author, LocalDateTime createdDate,
                                         String imgLink, Long loverCnt, Long loverYn) {
        this.memberId = memberId;
        this.imageId = imageId;
        this.author = author;
        this.createdDate = createdDate;
        this.imgLink = imgLink;
        this.loverCnt = loverCnt;
        this.loverYn = loverYn;
    }
    public ImageArticleDetailResponseDto(Long memberId, Long imageId, String author, LocalDateTime createdDate,
                                         String imgLink, Long loverCnt) {
        this.memberId = memberId;
        this.imageId = imageId;
        this.author = author;
        this.createdDate = createdDate;
        this.imgLink = imgLink;
        this.loverCnt = loverCnt;
        this.loverYn = 0L;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

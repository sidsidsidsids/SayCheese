package com.reminiscence.article.imagearticle.dto;

import com.reminiscence.article.domain.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageArticleDetailResponseDto {

    private int isMine;
    private Long imageId;
    private String author;
    private LocalDateTime createdDate;
    private String imgLink;
    private Long loverCnt;
    private Long loverYn;
    private List<String> tags = new ArrayList<>();

    public ImageArticleDetailResponseDto(boolean isMine, Long imageId, String author, LocalDateTime createdDate,
                                         String imgLink, Long loverCnt, Long loverYn) {
        this.isMine = checkMine(isMine);
        this.imageId = imageId;
        this.author = author;
        this.createdDate = createdDate;
        this.imgLink = imgLink;
        this.loverCnt = loverCnt;
        if(loverYn == null)
            this.loverYn = 0L;
        else
            this.loverYn = 1L;
    }
    public ImageArticleDetailResponseDto(Long imageId, String author, LocalDateTime createdDate,
                                         String imgLink, Long loverCnt) {
        this.isMine = 0;
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
    private int checkMine(boolean isMine){
        if(isMine)
            return 1;
        else
            return 0;
    }
}

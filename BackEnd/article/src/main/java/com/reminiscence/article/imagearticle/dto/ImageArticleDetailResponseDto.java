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
    private String name;
    private LocalDateTime createdDate;
    private String imgLink;
    private Long loverCnt;
    private Long loverYn;
    private List<String> tags = new ArrayList<>();

    public ImageArticleDetailResponseDto(Long memberId, Long imageId, String name, LocalDateTime createdDate,
                                         String imgLink, Long loverCnt, Long loverYn) {
        this.memberId = memberId;
        this.imageId = imageId;
        this.name = name;
        this.createdDate = createdDate;
        this.imgLink = imgLink;
        this.loverCnt = loverCnt;
        this.loverYn = loverYn;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}

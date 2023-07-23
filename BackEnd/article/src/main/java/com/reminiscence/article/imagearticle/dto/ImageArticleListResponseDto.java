package com.reminiscence.article.imagearticle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ImageArticleListResponseDto {

    private String imageLink;
    private Long loverCnt;
    private LocalDateTime createdDate;
    private String author;


    public ImageArticleListResponseDto(String imageLink, Long loverCnt, LocalDateTime createdDate, String author) {
        this.imageLink = imageLink;
        this.loverCnt = loverCnt;
        this.createdDate = createdDate;
        this.author = author;
    }
}

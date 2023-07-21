package com.reminiscence.article.imagearticle.dto;

import com.reminiscence.article.domain.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ImageArticleDetailResponseDto {

    private Long memberId;
    private String name;
    private LocalDateTime createdDate;
    private String imgLink;
    private int loverCnt;
    private int lover_yn;
    private List<String> tags = new ArrayList<>();

    public ImageArticleDetailResponseDto(ImageArticle imageArticle) {
        this.memberId = imageArticle.getMember().getId();
        this.name = imageArticle.getMember().getNickname();
        this.createdDate = imageArticle.getCreatedDate();
        this.imgLink = imageArticle.getImage().getLink();
        this.lover_yn = imageArticle.getMember().getLovers().size();
        this.loverCnt= imageArticle.getLovers().size();
        for (ImageTag imageTag : imageArticle.getImage().getImageTags()) {
            this.tags.add(imageTag.getTag().getName());
        }
    }
}

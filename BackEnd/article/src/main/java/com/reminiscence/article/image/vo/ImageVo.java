package com.reminiscence.article.image.vo;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.NoticeArticle;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
public class ImageVo implements Serializable{

    private final Long imageId;
    private final String imageLink;
    private final LocalDateTime createdDate;

    public ImageVo(Image image){
        this.imageId=image.getId();
        this.imageLink= image.getLink();
        this.createdDate=image.getCreatedDate();
    }


}

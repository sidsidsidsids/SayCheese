package com.reminiscence.article.image.vo;


import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageArticle;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class ImageVo implements Serializable{

    private final Long imageId;
    private final String imageLink;
    private final LocalDateTime createdDate;
    private final Long loverCnt;
    private final Character articleYn;

    public ImageVo(Long imageId, String imageLink, LocalDateTime createdDate,
                   Long articleId, long loverCnt) {
        this.imageId = imageId;
        this.imageLink = imageLink;
        this.createdDate = createdDate;
        this.loverCnt = loverCnt;
        if(articleId == null)
            this.articleYn = 'N';
        else{
            this.articleYn = 'Y';
        }
    }
}

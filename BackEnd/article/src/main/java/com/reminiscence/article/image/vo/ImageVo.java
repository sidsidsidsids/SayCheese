package com.reminiscence.article.image.vo;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.ImageOwner;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ImageVo implements Serializable{

    private final Long imageId;
    private final String imageLink;
    private final LocalDateTime createdDate;
    private final int loverCnt;

    public ImageVo(Long imageId, String imageLink, LocalDateTime createdDate, ImageArticle image) {
        this.imageId = imageId;
        this.imageLink = imageLink;
        this.createdDate = createdDate;
//        this.loverCnt = loverCnt;
        if(image == null)
            this.loverCnt = -1;
        else{
            this.loverCnt = image.getLovers().size();
        }

//        if(image.getImageOwners() == null){
//            this.loverCnt = -1;
//        }else{
//            this.loverCnt = image.getImageOwners().size();
//        }
    }
}

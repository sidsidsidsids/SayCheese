package com.reminiscence.article.image.dummy;

import com.reminiscence.article.ImageArticle.dummy.DummyImageArticleDeleteRequestDto;
import com.reminiscence.article.image.dto.ImageType;

public class DummyImagePreSignRequestDto {
    private String imageName;
    private ImageType imageType;

    public static class Builder{
        private String imageName;
        private ImageType imageType;

        public void imageName(String imageName){
            this.imageName = imageName;
        }
        public void imageType(ImageType imageType){
            this.imageType = imageType;
        }
        public DummyImagePreSignRequestDto build(){
            return new DummyImagePreSignRequestDto(this);
        }
    }

    private DummyImagePreSignRequestDto(DummyImagePreSignRequestDto.Builder builder) {
        this.imageName = builder.imageName;
        this.imageType = builder.imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public ImageType getImageType() {
        return imageType;
    }
}

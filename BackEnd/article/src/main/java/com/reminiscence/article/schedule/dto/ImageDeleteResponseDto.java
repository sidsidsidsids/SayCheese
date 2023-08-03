package com.reminiscence.article.schedule.dto;

import lombok.Getter;

@Getter
public class ImageDeleteResponseDto {
    private String imageName;
    private String imageType;

    public ImageDeleteResponseDto(String imageName,String imageType) {
        this.imageName = imageName;
        this.imageType = imageType;
    }
}

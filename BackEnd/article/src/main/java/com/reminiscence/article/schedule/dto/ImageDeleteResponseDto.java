package com.reminiscence.article.schedule.dto;

import lombok.Getter;

@Getter
public class ImageDeleteResponseDto {
    private Long imageId;
    private String imageName;
    private String imageType;

    public ImageDeleteResponseDto(Long imageId, String imageName,String imageType) {
        this.imageId = imageId;
        this.imageName = imageName;
        this.imageType = imageType;
    }
}

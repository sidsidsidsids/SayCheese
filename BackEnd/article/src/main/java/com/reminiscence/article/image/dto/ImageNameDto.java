package com.reminiscence.article.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageNameDto {
    private String imageName;
    private ImageType imageType;

    public ImageNameDto(String imageName, ImageType imageType) {
        this.imageName = imageName;
        this.imageType = imageType;
    }
}

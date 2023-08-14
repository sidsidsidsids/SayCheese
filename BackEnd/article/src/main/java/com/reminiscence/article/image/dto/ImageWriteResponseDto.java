package com.reminiscence.article.image.dto;

import lombok.Getter;

@Getter
public class ImageWriteResponseDto {
    private Long imageId;

    public ImageWriteResponseDto(Long imageId) {
        this.imageId = imageId;
    }
}

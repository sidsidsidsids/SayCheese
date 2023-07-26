package com.reminiscence.article.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PreSignedRequestDto {
    @NotBlank
    private String imageName;

    @NotNull
    private ImageType imageType;

    public PreSignedRequestDto(String imageName, ImageType imageType) {
        this.imageName = imageName;
        this.imageType = imageType;
    }
}

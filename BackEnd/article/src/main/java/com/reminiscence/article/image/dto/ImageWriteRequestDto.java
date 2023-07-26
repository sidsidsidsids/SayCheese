package com.reminiscence.article.image.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class ImageWriteRequestDto {
    @Size(max = 1000, message = "이미지 링크는 1000자를 넘을 수 없습니다.")
    @NotBlank(message = "이미지 링크를 입력해주세요.")
    private String imageLink;

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String imageName;
}

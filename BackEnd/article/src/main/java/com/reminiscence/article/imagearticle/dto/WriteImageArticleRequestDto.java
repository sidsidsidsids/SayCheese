package com.reminiscence.article.imagearticle.dto;

import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
public class WriteImageArticleRequestDto {
    @Positive(message = "이미지 번호는 양수입니다.")
    private Long imageId;

}

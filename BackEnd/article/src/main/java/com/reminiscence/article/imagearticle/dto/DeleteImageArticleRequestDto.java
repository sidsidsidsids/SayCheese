package com.reminiscence.article.imagearticle.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
public class DeleteImageArticleRequestDto {
    @Positive(message = "게시글 번호는 양수입니다.")
    @NotNull(message = "이미지 게시글 번호를 입력해주세요.")
    private Long articleId;
}

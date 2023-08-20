package com.reminiscence.article.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DeleteImageOwnerRequestDto {
    @NotNull(message = "이미지 아이디를 입력해주세요.")
    public Long imageId;

    public DeleteImageOwnerRequestDto(Long imageId) {
        this.imageId = imageId;
    }
}

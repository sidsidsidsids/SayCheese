package com.reminiscence.article.amazon.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class PreSignedRequestDto{
    @NotBlank(message="프레임 이름을 입력해주세요")
    private String fileName;

    @NotNull(message="프레임 타입을 선택해주세요")
    FileType fileType;


}
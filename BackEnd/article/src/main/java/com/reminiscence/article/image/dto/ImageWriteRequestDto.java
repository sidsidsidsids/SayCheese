package com.reminiscence.article.image.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class ImageWriteRequestDto {
    @Size(max = 1000, message = "이미지 링크는 1000자를 넘을 수 없습니다.")
    @NotBlank(message = "이미지 링크를 입력해주세요.")
    private String imageLink;

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String imageName;

    @NotNull(message = "태그를 입력해주세요.")
    @NotEmpty(message = "태그를 입력해주세요.")
    private List<Long> tags;

    @NotBlank(message = "방 코드를 입력해주세요.")
    private String roomCode;
}

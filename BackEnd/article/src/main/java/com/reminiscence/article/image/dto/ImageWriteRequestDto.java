package com.reminiscence.article.image.dto;

import com.reminiscence.article.amazon.dto.FileType;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class ImageWriteRequestDto {
    @NotNull(message = "이미지 파일 타입(Image, Frame, Profile)을 입력해주세요.")
    private FileType fileType;

    @NotBlank(message = "이미지 파일 이름을 입력해주세요.")
    private String imageName;

    @NotNull(message = "태그를 입력해주세요.")
    private List<Long> tags;

    @NotBlank(message = "방 코드를 입력해주세요.")
    private String roomCode;
}

package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameSpecification;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class FrameArticleRequestDto {
    @Size(min=3, max=100, message="프레임 이름은 최소 3글자, 100글자 이하로 작성하세요")
    @NotBlank(message = "프레임 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "파일 타입을 입력해주세요")
    private String fileType;

    @NotNull(message="허용 여부를 선택해주세요")
    private Boolean isPublic;

    @NotNull(message="규격을 선택해주세요")
    private FrameSpecification frameSpecification;

    @Size(min=3, max=20, message="제목은 최소 3글자, 20글자 이하로 작성하세요")
    @NotBlank(message = "제목을 입력해주세요")
    private String subject;

}

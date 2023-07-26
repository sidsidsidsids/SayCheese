package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.Frame;
import com.reminiscence.article.domain.FrameSpecification;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class FrameArticleRequestDto {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "Link를 입력해주세요")
    private String link;
    @NotNull(message="허용 여부를 선택해주세요")
    private Boolean isPublic;

    @NotNull(message="규격을 선택해주세요")
    private FrameSpecification frameSpecification;

    @NotBlank(message = "제목을 입력해주세요")
    private String subject;

}

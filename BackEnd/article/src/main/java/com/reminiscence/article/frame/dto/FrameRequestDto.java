package com.reminiscence.article.frame.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class FrameRequestDto {
    @NotBlank(message="프레임 이름을 입력해주세요")
    private String frameName;

    @NotNull(message="프레임 타입을 선택해주세요")
    FrameType frameType;


}

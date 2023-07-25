package com.reminiscence.article.frame.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Getter
@NoArgsConstructor
public class FrameRequestDto {
    private String frameName;
    FrameType frameType;


}

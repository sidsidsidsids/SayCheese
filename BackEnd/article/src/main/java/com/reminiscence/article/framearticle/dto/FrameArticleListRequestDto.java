package com.reminiscence.article.framearticle.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
public class FrameArticleListRequestDto {

    @Null
    private String searchWord;

}

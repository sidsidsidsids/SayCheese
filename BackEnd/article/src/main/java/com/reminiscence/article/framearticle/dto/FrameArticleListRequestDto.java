package com.reminiscence.article.framearticle.dto;

import lombok.Getter;

import javax.validation.constraints.Null;

@Getter
public class FrameArticleListRequestDto {

    @Null
    private String searchWord;

}

package com.reminiscence.article.image.dto;

import lombok.Getter;

@Getter
public class RandomTagResponseDto {

    private Long id;
    private String tag;

    public RandomTagResponseDto(Long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

}

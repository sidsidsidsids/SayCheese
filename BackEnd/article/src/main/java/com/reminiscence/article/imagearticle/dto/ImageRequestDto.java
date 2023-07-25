package com.reminiscence.article.imagearticle.dto;

import com.reminiscence.article.domain.Image;
import lombok.Getter;

@Getter
public class ImageRequestDto {
    private String link;
    private String type;

    public Image toEntity() {
        return Image.builder()
                .link(link)
                .type(type)
                .build();
    }
}

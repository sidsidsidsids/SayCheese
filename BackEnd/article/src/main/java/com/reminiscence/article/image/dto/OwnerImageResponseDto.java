package com.reminiscence.article.image.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OwnerImageResponseDto {
    private Long imageId;
    private String imageLink;
    private LocalDateTime createdDate;

    public OwnerImageResponseDto(Long imageId, String imageLink, LocalDateTime createdDate) {
        this.imageId = imageId;
        this.imageLink = imageLink;
        this.createdDate = createdDate;
    }
}

package com.reminiscence.article.image.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PreSignedResponseDto {
    private String preSignUrl;
    private String fileName;

    public PreSignedResponseDto(String preSignUrl, String fileName) {
        this.preSignUrl = preSignUrl;
        this.fileName = fileName;
    }

}

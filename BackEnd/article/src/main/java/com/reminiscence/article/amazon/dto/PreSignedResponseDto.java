package com.reminiscence.article.amazon.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PreSignedResponseDto {
    private String preSignUrl;
    private String fileName;

    @Builder
    public PreSignedResponseDto(String preSignUrl, String fileName) {
        this.preSignUrl = preSignUrl;
        this.fileName = fileName;
    }

}


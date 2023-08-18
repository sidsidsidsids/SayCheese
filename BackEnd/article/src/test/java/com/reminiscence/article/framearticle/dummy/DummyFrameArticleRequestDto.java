package com.reminiscence.article.framearticle.dummy;


import com.reminiscence.article.domain.FrameSpecification;
import lombok.Builder;
import lombok.Getter;


@Getter
public class DummyFrameArticleRequestDto {
    private String name;
    private String fileType;
    private Boolean isPublic;
    private String frameSpecification;
    private String subject;


    @Builder
    public DummyFrameArticleRequestDto(String name, String fileType, Boolean isPublic, String frameSpecification, String subject) {
        this.name = name;
        this.fileType = fileType;
        this.isPublic = isPublic;
        this.frameSpecification = frameSpecification;
        this.subject = subject;
    }
}

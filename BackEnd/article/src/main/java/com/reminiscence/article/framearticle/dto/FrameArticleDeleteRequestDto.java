package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FrameArticleDeleteRequestDto {
    private Member member;
    private Long frameArticleId;

    @Builder
    public FrameArticleDeleteRequestDto(Member member, Long frameArticleId) {
        this.member = member;
        this.frameArticleId = frameArticleId;
    }
}

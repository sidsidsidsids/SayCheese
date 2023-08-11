package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
public class FrameArticleAlterPublicRequestDto {
    private Member member;
    private Long frameArticleId;

    @Builder
    public FrameArticleAlterPublicRequestDto(Member member, Long frameArticleId) {
        this.member = member;
        this.frameArticleId = frameArticleId;
    }
}

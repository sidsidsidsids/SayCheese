package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FrameArticleReadRequestDto {
    private UserDetail userDetail;
    private Long frameArticleId;

    @Builder
    public FrameArticleReadRequestDto(UserDetail userDetail, Long frameArticleId) {
        this.userDetail = userDetail;
        this.frameArticleId = frameArticleId;
    }
}

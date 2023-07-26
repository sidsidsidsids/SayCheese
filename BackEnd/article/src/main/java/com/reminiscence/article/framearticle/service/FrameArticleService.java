package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;

public interface FrameArticleService {

    void saveFrame(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);
}

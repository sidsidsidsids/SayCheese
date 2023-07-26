package com.reminiscence.article.frame.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;
import com.reminiscence.article.frame.dto.PreSignedResponseDto;

public interface FrameService {
    PreSignedResponseDto getPreSignedUrl(String bucket, String prefix, String fileName);

    void saveFrame(UserDetail userDetail, FrameArticleRequestDto frameArticleRequestDto);
}

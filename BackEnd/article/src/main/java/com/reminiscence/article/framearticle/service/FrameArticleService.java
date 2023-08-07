package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.FrameArticleAndMemberRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleDeleteRequestDto;
import com.reminiscence.article.framearticle.dto.FrameArticleListResponseDto;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;

import java.util.List;

public interface FrameArticleService {

    List<FrameArticleListResponseDto> getHotFrameArticleList(UserDetail userDetail);
    List<FrameArticleListResponseDto> getRecentFrameArticleList(UserDetail userDetail);
    List<FrameArticleListResponseDto> getRandomFrameArticleList(UserDetail userDetail);

    void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);

    void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto);
}

package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;

import java.util.List;

public interface FrameArticleService {

    List<FrameArticleListResponseDto> getHotFrameArticleList(UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);
    List<FrameArticleListResponseDto> getRecentFrameArticleList(UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);
    List<FrameArticleListResponseDto> getRandomFrameArticleList(UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);

    void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);

    void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto);
}

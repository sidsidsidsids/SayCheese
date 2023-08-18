package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.message.Response;
import org.springframework.data.domain.Pageable;

public interface FrameArticleService {

    FrameArticleListResponseDto getHotFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec);
    FrameArticleListResponseDto getRecentFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec);
    FrameArticleListResponseDto getRandomFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec);

    FrameArticleListResponseDto getMyFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord, String frameSpec);

    Response alterPublicStatusFrameArticle(FrameArticleAlterPublicRequestDto frameArticleAlterPublicRequestDto);

    void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);

    void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto);

    FrameArticleResponseDto readFrameArticle(FrameArticleReadRequestDto frameArticleReadRequestDto);
}

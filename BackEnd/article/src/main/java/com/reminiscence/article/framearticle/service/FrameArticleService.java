package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FrameArticleService {

    FrameArticleListResponseDto getHotFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord);
    FrameArticleListResponseDto getRecentFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord);
    FrameArticleListResponseDto getRandomFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord);

    FrameArticleListResponseDto getMyFrameArticleList(Pageable tempPageable, UserDetail userDetail, String searchWord);

    void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);

    void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto);
}

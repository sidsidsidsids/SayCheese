package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FrameArticleService {

    FrameArticleListResponseDto getHotFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);
    FrameArticleListResponseDto getRecentFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);
    FrameArticleListResponseDto getRandomFrameArticleList(Pageable tempPageable, UserDetail userDetail, FrameArticleListRequestDto frameArticleListRequestDto);

    void writeFrameArticle(FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto);

    void deleteFrameArticle(FrameArticleDeleteRequestDto frameArticleDeleteRequestDto);
}

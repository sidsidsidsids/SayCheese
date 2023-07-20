package com.reminiscence.article.notice.service;

import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface NoticeService {
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto);

    public void modifyArticle(Long noticeArticleId,NoticeArticleRequestDto noticeArticleRequestDto);

    public NoticeArticleResponseDto getNoticeArticleList(Pageable tempPageable);
}

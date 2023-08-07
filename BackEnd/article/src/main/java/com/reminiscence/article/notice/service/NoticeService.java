package com.reminiscence.article.notice.service;

import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleListResponseDto;
import com.reminiscence.article.notice.dto.NoticeArticleResponseDto;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto);

    public void modifyArticle(Long noticeArticleId,NoticeArticleRequestDto noticeArticleRequestDto);

    public NoticeArticleListResponseDto getNoticeArticleList(Pageable tempPageable);

    public NoticeArticleResponseDto getNoticeArticle(Long noticeArticleId);
}

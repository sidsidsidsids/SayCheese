package com.reminiscence.article.notice.service;

import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface NoticeService {
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto);

    public void modifyArticle(Long noticeArticleId,NoticeArticleRequestDto noticeArticleRequestDto);
}

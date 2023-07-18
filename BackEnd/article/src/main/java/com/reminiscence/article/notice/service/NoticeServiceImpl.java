package com.reminiscence.article.notice.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;

    @Override
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto) {
        NoticeArticle noticeArticle = NoticeArticleAndMemberRequestDto.toEntity(noticeArticleAndMemberRequestDto);
        noticeRepository.save(noticeArticle);
    }
}

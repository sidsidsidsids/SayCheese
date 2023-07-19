package com.reminiscence.article.notice.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.exception.customexception.NoticeException;
import com.reminiscence.article.exception.message.NoticeMessage;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;

    @Override
    public void writeArticle(NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto) {
        NoticeArticle noticeArticle = NoticeArticleAndMemberRequestDto.toEntity(noticeArticleAndMemberRequestDto);
        noticeRepository.save(noticeArticle);
    }

    @Override
    @Transactional
    public void modifyArticle(Long noticeArticleId, NoticeArticleRequestDto noticeArticleRequestDto) {
        NoticeArticle noticeArticle=noticeRepository.findById(noticeArticleId).orElseThrow(
                ()->new NoticeException(NoticeMessage.DATA_NOT_FOUND)
        );
        noticeArticle.modifySubject(noticeArticleRequestDto.getSubject());
        noticeArticle.modifyContent(noticeArticleRequestDto.getContent());
    }


}

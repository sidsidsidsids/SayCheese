package com.reminiscence.article.notice.service;

import com.reminiscence.article.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;
}

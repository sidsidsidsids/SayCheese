package com.reminiscence.article.notice.controller;

import com.reminiscence.article.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;

}

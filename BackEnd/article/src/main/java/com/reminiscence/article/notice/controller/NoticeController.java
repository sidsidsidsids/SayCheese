package com.reminiscence.article.notice.controller;

import com.reminiscence.article.common.MessageCode;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;


    @PostMapping("")
    public ResponseEntity<String> createNoticeArticle(@RequestBody NoticeArticleRequestDto noticeArticleRequestDto) {
        noticeService.writeArticle(noticeArticleRequestDto);
        return MessageCode.NOTICE_WRITE_SUCCESS.toResponseEntity();
    }
}

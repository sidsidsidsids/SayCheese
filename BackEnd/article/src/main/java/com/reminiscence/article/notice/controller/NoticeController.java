package com.reminiscence.article.notice.controller;

import com.reminiscence.article.common.MessageCode;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {
    private final NoticeService noticeService;


    @PostMapping("")
    public ResponseEntity<String> createNoticeArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                      @Valid @RequestBody NoticeArticleRequestDto noticeArticleRequestDto) {

        NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto=NoticeArticleAndMemberRequestDto.builder()
                .member(userDetail.getMember())
                .noticeArticleRequestDto(noticeArticleRequestDto).build();
        noticeService.writeArticle(noticeArticleAndMemberRequestDto);
        return MessageCode.NOTICE_WRITE_SUCCESS.toResponseEntity();
    }
}

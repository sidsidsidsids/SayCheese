package com.reminiscence.article.notice.controller;

import com.reminiscence.article.common.MessageCode;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/notice")
public class NoticeController {
    private final NoticeService noticeService;


    /**
     * 관리자가 공지 글을 작성하는 API
     * @param userDetail 사용자 정보
     * @param noticeArticleRequestDto 사용자 공지 글 작성 요청 정보
     * @return
     */
    @PostMapping("")
    public ResponseEntity<MessageCode.Response> writeNoticeArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                                   @Valid @RequestBody NoticeArticleRequestDto noticeArticleRequestDto) throws Exception{
        // user 정보와 사용자 요청 정보를 하나의 Dto로 만듬
        NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto=NoticeArticleAndMemberRequestDto.builder()
                .member(userDetail.getMember())
                .noticeArticleRequestDto(noticeArticleRequestDto).build();
        noticeService.writeArticle(noticeArticleAndMemberRequestDto);
        return MessageCode.NOTICE_WRITE_SUCCESS.toResponseEntity();
    }
    @PutMapping("/{noticeArticleId}")
    public ResponseEntity<MessageCode.Response> modifyNoticeArticle(@PathVariable String noticeArticleId,
                                                                   @Valid @RequestBody NoticeArticleRequestDto noticeArticleRequestDto) throws Exception{
        // user 정보와 사용자 요청 정보를 하나의 Dto로 만듬
        noticeService.modifyArticle(Long.parseLong(noticeArticleId),noticeArticleRequestDto);
        return MessageCode.NOTICE_MODIFY_SUCCESS.toResponseEntity();
    }
}

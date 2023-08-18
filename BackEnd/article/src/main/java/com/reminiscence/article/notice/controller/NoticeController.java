package com.reminiscence.article.notice.controller;

import com.reminiscence.article.domain.NoticeArticle;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.NoticeResponseMessage;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.notice.dto.NoticeArticleAndMemberRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleRequestDto;
import com.reminiscence.article.notice.dto.NoticeArticleListResponseDto;
import com.reminiscence.article.notice.dto.NoticeArticleResponseDto;
import com.reminiscence.article.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Response> writeNoticeArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                       @Valid @RequestBody NoticeArticleRequestDto noticeArticleRequestDto) throws Exception{
        // user 정보와 사용자 요청 정보를 하나의 Dto로 만듬
        NoticeArticleAndMemberRequestDto noticeArticleAndMemberRequestDto=NoticeArticleAndMemberRequestDto.builder()
                .member(userDetail.getMember())
                .noticeArticleRequestDto(noticeArticleRequestDto).build();
        noticeService.writeArticle(noticeArticleAndMemberRequestDto);
        return new ResponseEntity<>(Response.of(NoticeResponseMessage.NOTICE_WRITE_SUCCESS),HttpStatus.OK);
    }
    @PutMapping("/{noticeArticleId}")
    public ResponseEntity<Response> modifyNoticeArticle(@PathVariable String noticeArticleId,
                                                                              @Valid @RequestBody NoticeArticleRequestDto noticeArticleRequestDto) throws Exception{
        // user 정보와 사용자 요청 정보를 하나의 Dto로 만듬
        noticeService.modifyArticle(Long.parseLong(noticeArticleId),noticeArticleRequestDto);
        return new ResponseEntity<>(Response.of(NoticeResponseMessage.NOTICE_MODIFY_SUCCESS),HttpStatus.OK);
    }
    @GetMapping("")
    public ResponseEntity<NoticeArticleListResponseDto> getNoticeArticleList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable) throws Exception{
        NoticeArticleListResponseDto noticeArticleListResponseDto =noticeService.getNoticeArticleList(pageable);
        return new ResponseEntity<>(noticeArticleListResponseDto, HttpStatus.OK);
    }
    @GetMapping("/{noticeArticleId}")
    public ResponseEntity<NoticeArticleResponseDto> getNoticeArticle(@PathVariable String noticeArticleId) throws Exception{
        NoticeArticleResponseDto noticeArticleResponseDto=noticeService.getNoticeArticle(Long.parseLong(noticeArticleId));
        return new ResponseEntity<>(noticeArticleResponseDto, HttpStatus.OK);
    }
}

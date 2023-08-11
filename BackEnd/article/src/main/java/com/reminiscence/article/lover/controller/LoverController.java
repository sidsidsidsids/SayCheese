package com.reminiscence.article.lover.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.lover.service.LoverService;

import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.LoverResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/lover")
public class LoverController {

    private final LoverService loverService;

    /**
     *
     * @param
     * userDetail : 로그인한 사용자의 정보를 담고 있는 객체
     * @param
     * articleId
     *      id : 좋아요를 누른 게시글의 id
     * @return
     * ResponseEntity<Response> : 좋아요 추가 완료 메세지
     * @Exception
     *  NOT_FOUND_IMAGE_ARTICLE : 존재하지 않는 게시글을 좋아요를 누른 경우
     *
     */
    @PostMapping("{articleType}/{articleId}")
    public ResponseEntity<Response> writeLoverArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                      @PathVariable("articleType") String articleType,
                                                      @PathVariable("articleId") Long articleId){
        loverService.writeLoverArticle(userDetail.getMember().getId(),articleId, articleType);
        return new ResponseEntity<>(Response.of(LoverResponseMessage.LOVER_CLICK_SUCCESS),HttpStatus.OK);
    }


    /**
     *
     * @param
     * userDetail : 로그인한 사용자의 정보를 담고 있는 객체
     * @param
     * articleId
     *      id : 좋아요를 눌렀던 게시글의 id
     * @return
     * ResponseEntity<Response> : 좋아요 취소 완료 메세지
     * @Exception
     *  NOT_FOUND_LOVER : 존재하지 않는 좋아요를 취소하려는 경우
     *
     */
    @DeleteMapping("/{articleId}")
    public ResponseEntity<Response> deleteLoverArticle(@AuthenticationPrincipal UserDetail userDetail , @PathVariable("articleId") Long articleId){
        loverService.deleteLoverArticle(userDetail.getMember().getId(),articleId);
        return new ResponseEntity<>(Response.of(LoverResponseMessage.LOVER_CANCEL_MESSAGE), HttpStatus.OK);

    }
}

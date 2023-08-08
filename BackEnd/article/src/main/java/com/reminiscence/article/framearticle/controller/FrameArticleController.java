package com.reminiscence.article.framearticle.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.framearticle.service.FrameArticleService;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.FrameResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/frame")
public class FrameArticleController {
    private final FrameArticleService frameArticleService;

    /**
     * 게시글 목록 무작위 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  frameLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/random")
    public ResponseEntity<List<FrameArticleListResponseDto>> readRandomFrameArticleList(@AuthenticationPrincipal UserDetail userDetail, @RequestBody FrameArticleListRequestDto frameArticleListRequestDto){
        List<FrameArticleListResponseDto> randomImageArticleList = frameArticleService.getRandomFrameArticleList(userDetail, frameArticleListRequestDto);
        return new ResponseEntity<>(randomImageArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 좋아요 내림차순 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/hot")
    public ResponseEntity<List<FrameArticleListResponseDto>> readHotFrameArticleList(@AuthenticationPrincipal UserDetail userDetail, @RequestBody FrameArticleListRequestDto frameArticleListRequestDto) {
        List<FrameArticleListResponseDto> hotImageArticleList = frameArticleService.getHotFrameArticleList(userDetail, frameArticleListRequestDto);
        return new ResponseEntity<>(hotImageArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 최신순 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/recent")
    public ResponseEntity<List<FrameArticleListResponseDto>> readRecentFrameArticleList(@AuthenticationPrincipal UserDetail userDetail, @RequestBody FrameArticleListRequestDto frameArticleListRequestDto) {
        List<FrameArticleListResponseDto> recentImageArticleList = frameArticleService.getRecentFrameArticleList(userDetail, frameArticleListRequestDto);
        return new ResponseEntity<>(recentImageArticleList, HttpStatus.OK);
    }

    /**
     * 프레임 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * frameArticleRequestDto : 프레임 정보를 담은 Dto
     *     frameName : 프레임 파일 이름
     *     frameType : 프레임 타입(Frame, Image)
     * @return
     * Response : HttpStatus.OK
     */
    @PostMapping("")
    public ResponseEntity<Response> writeFrameArticle(@AuthenticationPrincipal UserDetail userDetail,
                                              @Valid @RequestBody FrameArticleRequestDto frameArticleRequestDto) {
        FrameArticleAndMemberRequestDto frameArticleAndMemberRequestDto= FrameArticleAndMemberRequestDto.builder()
                .member(userDetail.getMember())
                .frameArticleRequestDto(frameArticleRequestDto)
                .build();
        frameArticleService.writeFrameArticle(frameArticleAndMemberRequestDto);
        return new ResponseEntity<>(Response.of(FrameResponseMessage.FRAME_WRITE_SUCCESS),HttpStatus.OK);
    }

    @DeleteMapping("/{frameArticleId}")
    public ResponseEntity<Response> deleteFrameArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                       @PathVariable Long frameArticleId) {
        FrameArticleDeleteRequestDto frameArticleDeleteRequestDto=FrameArticleDeleteRequestDto.builder()
                .frameArticleId(frameArticleId)
                .member(userDetail.getMember())
                .build();
        frameArticleService.deleteFrameArticle(frameArticleDeleteRequestDto);
        return new ResponseEntity<>(Response.of(FrameResponseMessage.FRAME_DELETE_SUCCESS),HttpStatus.OK);
    }
}

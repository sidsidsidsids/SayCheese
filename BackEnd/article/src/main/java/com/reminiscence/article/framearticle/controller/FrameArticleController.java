package com.reminiscence.article.framearticle.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.*;
import com.reminiscence.article.framearticle.service.FrameArticleService;
import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.FrameResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
     * @param
     * searchWord : 검색어 (저자명 또는 제목)
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  frameLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/random")
    public ResponseEntity<FrameArticleListResponseDto> readRandomFrameArticleList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(required = false, defaultValue = "") String searchWord, @RequestParam(required = false, defaultValue = "") String frameSpec){
        FrameArticleListResponseDto randomFrameArticleList = frameArticleService.getRandomFrameArticleList(pageable, userDetail, searchWord, frameSpec);
        return new ResponseEntity<>(randomFrameArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 좋아요 내림차순 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @param
     * searchWord : 검색어 (저자명 또는 제목)
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/hot")
    public ResponseEntity<FrameArticleListResponseDto> readHotFrameArticleList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(required = false, defaultValue = "")  String searchWord, @RequestParam(required = false, defaultValue = "") String frameSpec) {
        FrameArticleListResponseDto hotFrameArticleList = frameArticleService.getHotFrameArticleList(pageable, userDetail, searchWord, frameSpec);
        return new ResponseEntity<>(hotFrameArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 최신순 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @param
     * searchWord : 검색어 (저자명 또는 제목)
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/recent")
    public ResponseEntity<FrameArticleListResponseDto> readRecentFrameArticleList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(required = false, defaultValue = "") String searchWord, @RequestParam(required = false, defaultValue = "") String frameSpec) {
        FrameArticleListResponseDto recentFrameArticleList = frameArticleService.getRecentFrameArticleList(pageable, userDetail, searchWord, frameSpec);
        return new ResponseEntity<>(recentFrameArticleList, HttpStatus.OK);
    }

    /**
     * 프레임 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * frameArticleRequestDto : 프레임 정보를 담은 Dto
     *     name : 프레임 파일 이름
     *     fileType : 프레임 타입(Frame, Image, Profile)
     *     isPublic : 공개 여부
     *     frameSpecification : 프레임 규격
     *     subject : 제목
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

    /**
     * 게시글 목록 최신순 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @param
     * searchWord : 검색어 (저자명 또는 제목)
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/my/list")
    public ResponseEntity<FrameArticleListResponseDto> readMyFrameArticleList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal UserDetail userDetail, @RequestParam(required = false, defaultValue = "") String searchWord, @RequestParam(required = false, defaultValue = "") String frameSpec) {
        FrameArticleListResponseDto recentFrameArticleList = frameArticleService.getMyFrameArticleList(pageable, userDetail, searchWord, frameSpec);
        return new ResponseEntity<>(recentFrameArticleList, HttpStatus.OK);
    }

    /**
     * 프레임 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * frameArticleId : 프레임 Article Id
     * @return
     * Response : HttpStatus.OK
     */
    @PutMapping("/{frameArticleId}")
    public ResponseEntity<Response> alterPublicStatusFrameArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                                  @PathVariable Long frameArticleId) {

        FrameArticleAlterPublicRequestDto frameArticleAlterPublicRequestDto =FrameArticleAlterPublicRequestDto.builder()
                .frameArticleId(frameArticleId)
                .member(userDetail.getMember())
                .build();
        return new ResponseEntity<>(frameArticleService.alterPublicStatusFrameArticle(frameArticleAlterPublicRequestDto),HttpStatus.OK);
    }

    /**
     * 프레임 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * frameArticleId : 프레임 Article Id
     * @return
     * Response : HttpStatus.OK
     */
    @GetMapping("/{frameArticleId}")
    public ResponseEntity readFrameArticle(@AuthenticationPrincipal UserDetail userDetail,
                                                                  @PathVariable Long frameArticleId) {

        FrameArticleReadRequestDto frameArticleReadRequestDto =FrameArticleReadRequestDto.builder()
                .frameArticleId(frameArticleId)
                .userDetail(userDetail)
                .build();

        return new ResponseEntity<>(frameArticleService.readFrameArticle(frameArticleReadRequestDto),HttpStatus.OK);
    }

}

package com.reminiscence.article.imagearticle.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.DeleteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import com.reminiscence.article.imagearticle.dto.WriteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.service.ImageArticleService;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.ImageArticleResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/image")
public class ImageArticleController {

    private final ImageArticleService imageService;

    /**
     * 게시글 목록 무작위 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/random")
    public ResponseEntity<List<ImageArticleListResponseDto>> readRandomImageArticleList(@AuthenticationPrincipal UserDetail userDetail){
        List<ImageArticleListResponseDto> randomImageArticleList = imageService.getRandomImageArticleList(userDetail);
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
    public ResponseEntity<List<ImageArticleListResponseDto>> readHotImageArticleList(@AuthenticationPrincipal UserDetail userDetail) {
        List<ImageArticleListResponseDto> hotImageArticleList = imageService.getHotImageArticleList(userDetail);
        return new ResponseEntity<>(hotImageArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 좋아요 최신순 조회 API
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
    public ResponseEntity<List<ImageArticleListResponseDto>> readRecentImageArticleList(@AuthenticationPrincipal UserDetail userDetail) {
        List<ImageArticleListResponseDto> recentImageArticleList = imageService.getRecentImageArticleList(userDetail);
        return new ResponseEntity<>(recentImageArticleList, HttpStatus.OK);
    }

    /**
     * 게시글 목록 무작위 태그 조회 API
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/tag")
    public ResponseEntity<List<ImageArticleListResponseDto>> readRandomTagImageArticleList(@AuthenticationPrincipal UserDetail userDetail) {
        List<ImageArticleListResponseDto> randomTagImageArticleList = imageService.getRandomTagImageArticleList(userDetail);
        return new ResponseEntity<>(randomTagImageArticleList, HttpStatus.OK);
    }
    /**
     * 게시글 목록 태그기준 조회 API
     * @param
     * tagId : 태그 ID
     * @param
     *  userDetail : 로그인한 사용자 정보
     * @Return
     * ImageArticleListResponseDto : 이미지 게시글 상세 정보
     *  imageLink : 이미지 링크
     *  loverCnt : 좋아요 수
     *  createdDate : 게시글 작성일
     *  author : 게시글 작성자
     */
    @GetMapping("/list/tag/{tagId}")
    public ResponseEntity<List<ImageArticleListResponseDto>> readTagImageArticleList(
            @PathVariable("tagId") Long tagId, @AuthenticationPrincipal UserDetail userDetail) {
        List<ImageArticleListResponseDto> tagImageArticleList = imageService.getTagImageArticleList(tagId,userDetail);
        return new ResponseEntity<>(tagImageArticleList, HttpStatus.OK);
    }

    /**
     * 게시글 상세보기 API
     * @param
     * articleId :이미지 게시글 ID
     * @return
     * ImageArticleDetailResponseDto : 이미지 게시글 상세 정보
     * memberId : 사용자 ID
     * name : 사용자 닉네임
     * createdDate : 게시글 작성일
     * imgLink : 이미지 링크
     * loverCnt : 좋아요 수
     * lover_yn : 좋아요 여부
     * tags : 태그 리스트
     */
    @GetMapping("/{articleId}")
    public ResponseEntity<ImageArticleDetailResponseDto> readImageArticleDetail(
            @PathVariable("articleId") Long articleId, @AuthenticationPrincipal UserDetail userDetail){
        ImageArticleDetailResponseDto  imageArticleDetail = imageService.getImageArticleDetail(articleId, userDetail);
        return new ResponseEntity<>(imageArticleDetail, HttpStatus.OK);
    }

    /**
     *
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : 이미지 게시글 작성 요청 정보
     *            imageId : 이미지 ID
     * @return
     * Response : 이미지 게시글 작성 성공 메시지, HttpStatus.OK
     */
    @PostMapping
    public ResponseEntity<Response> writeImageArticle(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid WriteImageArticleRequestDto requestDto) {
        imageService.writeImageArticle(requestDto.getImageId(), userDetail.getMember().getId());
        return new ResponseEntity<>(Response.of(ImageArticleResponseMessage.IMAGE_ARTICLE_WRITE_SUCCESS), HttpStatus.OK);
    }

    /**
     * 이미지 게시글만 삭제 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto
     *          articleId : 이미지 게시글 ID
     * @return
     * Response : 이미지 게시글 삭제 성공 메시지, HttpStatus.OK
     */
    @DeleteMapping
    public ResponseEntity<Response> deleteOnlyImageArticle(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid DeleteImageArticleRequestDto requestDto){
        imageService.deleteImageArticle(userDetail.getMember().getId(),requestDto.getArticleId());
        return new ResponseEntity<>(Response.of(ImageArticleResponseMessage.IMAGE_ARTICLE_DELETE_SUCCESS),HttpStatus.OK);
    }

    /**
     * 이미지 게시글 및 이미지 삭제 API
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto
     *    articleId : 이미지 게시글 ID
     * @return
     * Response : 이미지 게시글 및 이미지 삭제 성공 메시지, HttpStatus.OK
     */
    @DeleteMapping("/all")
    public ResponseEntity<Response> deleteImageAndArticle(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody @Valid DeleteImageArticleRequestDto requestDto){
        imageService.deleteImageAndArticle(userDetail.getMember().getId(),requestDto.getArticleId());
        return new ResponseEntity<>(Response.of(ImageArticleResponseMessage.IMAGE_ARTICLE_IMAGE_DELETE_SUCCESS),HttpStatus.OK);
    }

}

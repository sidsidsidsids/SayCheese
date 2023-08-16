package com.reminiscence.article.image.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.*;
import com.reminiscence.article.image.service.ImageService;
import com.reminiscence.article.message.Response;
import com.reminiscence.article.message.custom_message.ImageResponseMessage;
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
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    /**
     * 소유한 이미지 조회 최근순
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * pageable : 목록 조회 페이지 처리 데이터
     *         page : 페이지 번호
     * @return
     * Response : HttpStatus.OK
     */
    @GetMapping
    public ResponseEntity<OwnerImageListResponseDto> readOwnerRecentImages(
            @AuthenticationPrincipal UserDetail userDetail,
            @PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable){
        OwnerImageListResponseDto recentOwnImages = imageService.getReadRecentOwnImages(userDetail.getMember().getId(), pageable);
        return new ResponseEntity<>(recentOwnImages, HttpStatus.OK);
    }

    /**
     * 랜덤 태그 4개 조회

     * @return
     * Response : List<RandomTagResponseDto>
     *      id : 태그 ID
     *      tag : 태그
     */

    @GetMapping("/random/tag")
    public ResponseEntity<List<RandomTagResponseDto>> readRandomTag(){
        List<RandomTagResponseDto> randomTags = imageService.getRandomTags();
        return new ResponseEntity(randomTags, HttpStatus.OK);
    }


    /**
     * 이미지 정보 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : JWT 토큰으로 인증된 사용자 정보
     *     fileType : 이미지 분류(Image, Frame, Profile)
     *     imageName : 이미지 파일 이름
     *     roomCode : 방 코드
     *     tags : 태그 리스트
     * @return
     * Response : HttpStatus.OK
     */

    @PostMapping
    public ResponseEntity<ImageWriteResponseDto> writeImageInfo(@AuthenticationPrincipal UserDetail userDetail,
                                              @RequestBody @Valid ImageWriteRequestDto requestDto) {
        ImageWriteResponseDto responseDto = imageService.saveImage(userDetail, requestDto);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    /**
     * 이미지 소유자 삭제
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : JWT 토큰으로 인증된 사용자 정보
     *     imageId : 이미지 ID
     * @return
     * Response : HttpStatus.OK
     */
    @DeleteMapping
    public ResponseEntity<Response> deleteImageOwner(@AuthenticationPrincipal UserDetail userDetail,
                                                    @RequestBody @Valid DeleteImageOwnerRequestDto requestDto) {

        imageService.deleteImage(requestDto.getImageId(), userDetail.getMember().getId());
        return new ResponseEntity<>(Response.of(ImageResponseMessage.DELETE_IMAGE_SUCCESS), HttpStatus.OK);
    }

}

package com.reminiscence.article.image.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.ImageWriteRequestDto;
import com.reminiscence.article.image.dto.PreSignedRequestDto;
import com.reminiscence.article.image.dto.PreSignedResponseDto;
import com.reminiscence.article.image.service.ImageService;
import com.reminiscence.article.message.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    /**
     * AmazonS3 Presigned URL 생성
     * @param
     * imageNameDto : JWT 토큰으로 인증된 사용자 정보
     *     imageName : 이미지 파일 이름
     *     imageType : 이미지 타입(Frame, Image)
     * @return
     * PreSignedResponseDto
     *     preSignUrl : PreSignedUrl
     *     fileName : (UUID + 파일 이름)으로 설정된 저장될 파일 이름
     */
    @PostMapping("/presigned")
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(@RequestBody PreSignedRequestDto imageNameDto) {
        String prefix = imageNameDto.getImageType().getValue();
        String fileName = imageNameDto.getImageName();

        PreSignedResponseDto preSignedResponseDto = imageService.getPreSignedUrl(prefix, fileName);
        return new ResponseEntity<>(preSignedResponseDto, HttpStatus.OK);
    }

    /**
     * 이미지 저장
     * @param
     * userDetail : JWT 토큰으로 인증된 사용자 정보
     * @param
     * requestDto : JWT 토큰으로 인증된 사용자 정보
     *     imageLink : 이미지 링크
     *     imageName : 이미지 파일 이름
     * @return
     * Response : HttpStatus.OK
     */
    @PostMapping("/save")
    public ResponseEntity<Void> saveImage(@AuthenticationPrincipal UserDetail userDetail,
                                              @RequestBody ImageWriteRequestDto requestDto) {
        imageService.saveImage(userDetail, requestDto.getImageName(), requestDto.getImageLink());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

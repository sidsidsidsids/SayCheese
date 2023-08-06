package com.reminiscence.article.amazon.controller;

import com.reminiscence.article.amazon.dto.PreSignedRequestDto;
import com.reminiscence.article.amazon.dto.PreSignedResponseDto;
import com.reminiscence.article.amazon.service.AmazonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/amazon")
@RequiredArgsConstructor
public class AmazonController {
    private final AmazonService amazonService;

    /**
     * AmazonS3 Presigned URL 생성
     * @param
     * frameRequestDto
     *     fileName : 파일 이름
     *     fileType : Frame, Image, Profile 중 하나
     * @return
     * PreSignedResponseDto
     *     preSignUrl : PreSignedUrl
     *     fileName : (UUID + 파일 이름)으로 설정된 저장될 파일 이름
     */
    @PostMapping("/presigned")
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(@Valid @RequestBody PreSignedRequestDto frameRequestDto) {
        String prefix = frameRequestDto.getFileType().getValue();
        String fileName = frameRequestDto.getFileName();

        PreSignedResponseDto preSignedResponseDto = amazonService.getPreSignedUrl(prefix, fileName);
        return new ResponseEntity<>(preSignedResponseDto, HttpStatus.OK);
    }

}

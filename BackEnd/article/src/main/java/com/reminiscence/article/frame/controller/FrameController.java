package com.reminiscence.article.frame.controller;

import com.reminiscence.article.frame.dto.FrameRequestDto;
import com.reminiscence.article.frame.dto.PreSignedResponseDto;
import com.reminiscence.article.frame.service.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/frame")
public class FrameController {
    private final FrameService frameService;

    /**
     * AmazonS3 Presigned URL 생성
     * @param
     * frameRequestDto
     *     frameName : 프레임 이름
     *     frameType : Frame, Image
     * @return
     * PreSignedResponseDto
     *     preSignUrl : PreSignedUrl
     *     fileName : (UUID + 파일 이름)으로 설정된 저장될 파일 이름
     */
    @PostMapping("/presigned")
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(@Valid @RequestBody FrameRequestDto frameRequestDto) {
        String prefix = frameRequestDto.getFrameType().getValue();
        String fileName = frameRequestDto.getFrameName();

        PreSignedResponseDto preSignedResponseDto = frameService.getPreSignedUrl("200oks3bucket", prefix, fileName);
        return new ResponseEntity<>(preSignedResponseDto, HttpStatus.OK);
    }

}

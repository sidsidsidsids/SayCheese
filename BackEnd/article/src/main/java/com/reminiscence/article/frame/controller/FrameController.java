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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/frame")
public class FrameController {
    private final FrameService frameService;

    @PostMapping("/presigned")
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(@RequestBody FrameRequestDto frameRequestDto) {
        String prefix = frameRequestDto.getFrameType().getValue();
        String fileName = frameRequestDto.getFrameName();

//        PreSignedResponseDto preSignedResponseDto = frameService.getPreSignedUrl("200oks3bucket", prefix, fileName);
//        return new ResponseEntity<>(preSignedResponseDto, HttpStatus.OK);
    }
}

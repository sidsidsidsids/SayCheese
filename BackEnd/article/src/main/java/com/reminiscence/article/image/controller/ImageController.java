package com.reminiscence.article.image.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.ImageNameDto;
import com.reminiscence.article.image.dto.PreSignedResponseDto;
import com.reminiscence.article.image.service.ImageService;
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
    @PostMapping("/presigned")
    public ResponseEntity<PreSignedResponseDto> getPreSignedUrl(@RequestBody ImageNameDto imageNameDto) {
        String prefix = imageNameDto.getImageType().getValue();
        String fileName = imageNameDto.getImageName();

        PreSignedResponseDto preSignedResponseDto = imageService.getPreSignedUrl("200oks3bucket", prefix, fileName);
        return new ResponseEntity<>(preSignedResponseDto, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Void> saveImage(@AuthenticationPrincipal UserDetail userDetail,
                                          @RequestBody ImageNameDto imageNameDto) {
        imageService.saveImage(userDetail, imageNameDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

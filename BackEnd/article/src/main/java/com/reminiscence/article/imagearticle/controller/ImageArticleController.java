package com.reminiscence.article.imagearticle.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.DeleteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.dto.WriteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.service.ImageArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/image")
public class ImageArticleController {

    private final ImageArticleService imageService;

    @GetMapping("/{articleId}")
    public ResponseEntity<ImageArticleDetailResponseDto> getImageArticleDetail(
            @PathVariable("articleId") Long articleId) {
        ImageArticleDetailResponseDto  imageArticleDetail = imageService.getImageArticleDetail(articleId);
        return ResponseEntity.ok(imageArticleDetail);
    }
    @PostMapping
    public ResponseEntity<Void> writeImageArticle(
            @AuthenticationPrincipal UserDetail userDetail,
            @RequestBody WriteImageArticleRequestDto requestDto) {
        imageService.writeImageArticle(userDetail.getMember().getId(), requestDto.getImageId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteImageArticle(
            @RequestBody DeleteImageArticleRequestDto requestDto){
        imageService.deleteImageArticle(requestDto.getArticleId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteImageAndArticle(
            @RequestBody DeleteImageArticleRequestDto requestDto){
        imageService.deleteImageAndArticle(requestDto.getArticleId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

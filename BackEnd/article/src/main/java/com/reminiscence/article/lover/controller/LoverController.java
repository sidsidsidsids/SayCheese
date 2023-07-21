package com.reminiscence.article.lover.controller;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.lover.dto.LoverRequestDto;
import com.reminiscence.article.lover.service.LoverService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/lover")
public class LoverController {

    private final LoverService loverService;
    @PostMapping("/image")
    public ResponseEntity<Void> writeLoverImageArticle(@AuthenticationPrincipal UserDetail userDetail, @RequestBody LoverRequestDto requestDto){
        if(loverService.writeLoverImageArticle(userDetail.getMember().getId(),requestDto.getId())){
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/image")
    public ResponseEntity<Void> deleteLoverImageArticle(@AuthenticationPrincipal UserDetail userDetail ,@RequestBody LoverRequestDto requestDto){
        if(loverService.deleteLoverImageArticle(userDetail.getMember().getId(),requestDto.getId())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

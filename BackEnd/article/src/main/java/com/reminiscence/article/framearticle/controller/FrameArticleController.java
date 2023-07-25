package com.reminiscence.article.framearticle.controller;

import com.reminiscence.article.framearticle.service.FrameArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article/frame")
public class FrameArticleController {
    private final FrameArticleService frameArticleService;
}

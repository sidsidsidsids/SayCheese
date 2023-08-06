package com.reminiscence.article.frame.controller;

import com.reminiscence.article.frame.service.FrameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/frame")
public class FrameController {
    private final FrameService frameService;

}

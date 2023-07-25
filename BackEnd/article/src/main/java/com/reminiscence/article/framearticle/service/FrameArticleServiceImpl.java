package com.reminiscence.article.framearticle.service;

import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FrameArticleServiceImpl implements FrameArticleService {
    private final FrameArticleRepository frameArticleRepository;
}

package com.reminiscence.article.frame.service;

import com.reminiscence.article.frame.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FrameServiceImpl implements FrameService {

    private final FrameRepository frameRepository;


}

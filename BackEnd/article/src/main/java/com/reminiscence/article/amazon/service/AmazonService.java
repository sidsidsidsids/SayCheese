package com.reminiscence.article.amazon.service;

import com.reminiscence.article.amazon.dto.PreSignedResponseDto;

public interface AmazonService {
    public PreSignedResponseDto getPreSignedUrl(String prefix, String fileName);
}

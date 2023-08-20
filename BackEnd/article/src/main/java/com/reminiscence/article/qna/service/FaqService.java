package com.reminiscence.article.qna.service;

import com.reminiscence.article.qna.dto.FaqListResponseDto;
import org.springframework.data.domain.Pageable;

public interface FaqService {
    FaqListResponseDto getFaqList(Pageable pageable);
}

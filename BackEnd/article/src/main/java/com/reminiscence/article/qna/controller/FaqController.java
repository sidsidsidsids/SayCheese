package com.reminiscence.article.qna.controller;

import com.reminiscence.article.qna.dto.FaqListResponseDto;
import com.reminiscence.article.qna.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faq")
public class FaqController {

    private final FaqService faqService;

    @GetMapping("")
    public ResponseEntity<FaqListResponseDto> getFaqList(@PageableDefault(size=10, page=1, direction = Sort.Direction.DESC) Pageable pageable){
        FaqListResponseDto faqListResponseDto = faqService.getFaqList(pageable);
        return new ResponseEntity<>(faqListResponseDto, HttpStatus.OK);
    }

}

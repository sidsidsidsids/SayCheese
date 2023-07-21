package com.reminiscence.article.qna.service;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.domain.FrequentlyAskedQuestion;
import com.reminiscence.article.qna.dto.FaqListResponseDto;
import com.reminiscence.article.qna.repository.FaqRepository;
import com.reminiscence.article.qna.vo.FaqVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    @Override
    public FaqListResponseDto getFaqList(Pageable tempPageable) {
        int page=tempPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_QNA_PER_PAGE_SIZE, Sort.Direction.DESC,"id");

        Page<FrequentlyAskedQuestion> pageList=faqRepository.findAll(pageable);

        FaqListResponseDto faqListResponseDto=new FaqListResponseDto(page, pageList.getTotalPages(),pageList.getTotalElements());
        for(FrequentlyAskedQuestion faq:pageList.getContent()){
            faqListResponseDto.add(new FaqVo(faq));
        }
        return faqListResponseDto;
    }
}

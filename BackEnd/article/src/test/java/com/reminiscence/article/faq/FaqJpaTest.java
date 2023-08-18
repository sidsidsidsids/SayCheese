package com.reminiscence.article.faq;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.domain.FrequentlyAskedQuestion;
import com.reminiscence.article.qna.dto.FaqListResponseDto;
import com.reminiscence.article.qna.repository.FaqRepository;
import com.reminiscence.article.qna.vo.FaqVo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@DataJpaTest
public class FaqJpaTest {

    @Autowired
    private FaqRepository faqRepository;

    @Test
    @DisplayName("Faq 조회")
    public void faqListTest(){
        int page=3;
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_QNA_PER_PAGE_SIZE, Sort.Direction.DESC,"id");

        Page<FrequentlyAskedQuestion> pageList=faqRepository.findAll(pageable);

        FaqListResponseDto faqListResponseDto=new FaqListResponseDto(page, pageList.getTotalPages(),pageList.getTotalElements());
        for(FrequentlyAskedQuestion faq:pageList.getContent()){
            faqListResponseDto.add(new FaqVo(faq));
        }

        Assertions.assertThat(faqListResponseDto.getPageNavigator().getCurPage()).isEqualTo(3);
        Assertions.assertThat(faqListResponseDto.getPageNavigator().getTotalPages()).isEqualTo(15);
        Assertions.assertThat(faqListResponseDto.getPageNavigator().getTotalDataCount()).isEqualTo(150L);
        Assertions.assertThat(faqListResponseDto.getFaqVoList().size()).isEqualTo(10);


    }
}

package com.reminiscence.article.qna.dto;

import com.reminiscence.article.qna.vo.FaqVo;
import com.reminiscence.article.util.PageNavigator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FaqListResponseDto {
    private PageNavigator pageNavigator;
    private List<FaqVo> faqVoList;


    public FaqListResponseDto(int curPage, int totalPages, long totalElements) {
        this.pageNavigator=new PageNavigator(curPage,totalPages,totalElements);
        this.faqVoList=new ArrayList<>();
    }

    public void add(FaqVo faqVo){
        faqVoList.add(faqVo);
    }
}

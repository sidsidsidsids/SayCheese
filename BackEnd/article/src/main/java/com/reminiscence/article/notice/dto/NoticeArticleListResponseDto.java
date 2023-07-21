package com.reminiscence.article.notice.dto;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import com.reminiscence.article.util.PageNavigator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeArticleListResponseDto implements Serializable {

    private PageNavigator pageNavigator;
    List<NoticeArticleVo> noticeArticleVoList;

    public NoticeArticleListResponseDto(int curPage, int totalPages, long totalElements) {
        this.pageNavigator=new PageNavigator(curPage,totalPages,totalElements);
        this.noticeArticleVoList=new ArrayList<>();
    }
    public void add(NoticeArticleVo noticeArticleListVo){
        this.noticeArticleVoList.add(noticeArticleListVo);
    }




}

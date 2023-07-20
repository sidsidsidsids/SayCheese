package com.reminiscence.article.notice.dto;

import com.reminiscence.article.notice.vo.NoticeArticleListVo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeArticleResponseDto {

    private int curPage; // 현재 페이지
    private int totalPages;
    private long totalDataCount;
    List<NoticeArticleListVo> noticeArticleListVoList;

    public void setCurPage(int curPage) {
        if(curPage<=0){
            this.curPage=1;
            return;
        }
        this.curPage = curPage;
    }
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    public void setTotalDataCount(long totalDataCount) {
        this.totalDataCount = totalDataCount;
    }
    public void add(NoticeArticleListVo noticeArticleListVo){
        if(noticeArticleListVoList==null){
            noticeArticleListVoList=new ArrayList<>();
        }
        this.noticeArticleListVoList.add(noticeArticleListVo);
    }


}

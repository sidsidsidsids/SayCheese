package com.reminiscence.article.notice.dto;

import com.reminiscence.article.notice.vo.NoticeArticleVo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeArticleResponseDto implements Serializable {

    private int curPage;
    private int totalPages;
    private long totalDataCount;
    List<NoticeArticleVo> noticeArticleVoList;

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
    public void add(NoticeArticleVo noticeArticleListVo){
        if(noticeArticleVoList==null){
            noticeArticleVoList=new ArrayList<>();
        }
        this.noticeArticleVoList.add(noticeArticleListVo);
    }


}

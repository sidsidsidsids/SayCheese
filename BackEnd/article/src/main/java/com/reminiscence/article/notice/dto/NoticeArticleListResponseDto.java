package com.reminiscence.article.notice.dto;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class NoticeArticleListResponseDto implements Serializable {

    private int curPage;
    private int totalPages;
    private long totalDataCount;
    List<NoticeArticleVo> noticeArticleVoList;
    private boolean prevNavigation;
    private boolean nextNavigation;

    public NoticeArticleListResponseDto(int curPage, int totalPages, long totalElements) {
        this.curPage = curPage;
        if(curPage<=0){
            this.curPage=1;
        }
        this.totalPages = totalPages;
        this.totalDataCount = totalElements;
        this.noticeArticleVoList=new ArrayList<>();
        initPrevNavigation();
        initNextNavigation();
    }
    public void add(NoticeArticleVo noticeArticleListVo){
        this.noticeArticleVoList.add(noticeArticleListVo);
    }

    private void initPrevNavigation(){

        this.prevNavigation=this.curPage<=Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE? false:true;

    }
    private void initNextNavigation(){
        long totalPageCount=(long)Math.ceil(this.curPage/(double)Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE)*Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE;
        this.nextNavigation=totalPageCount>=this.totalPages? false:true;
    }


}

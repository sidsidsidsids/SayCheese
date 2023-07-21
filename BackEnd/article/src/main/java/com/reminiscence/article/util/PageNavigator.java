package com.reminiscence.article.util;

import com.reminiscence.article.common.Pagination;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class PageNavigator {
    private int curPage;
    private int totalPages;
    private long totalDataCount;
    private boolean prevNavigation;
    private boolean nextNavigation;

    public PageNavigator(int curPage, int totalPages, long totalDataCount) {
        this.curPage = curPage;
        if(curPage<=0){
            this.curPage=1;
        }
        this.totalPages = totalPages;
        this.totalDataCount = totalDataCount;
        initPrevNavigation();
        initNextNavigation();
    }

    private void initPrevNavigation(){

        this.prevNavigation=this.curPage<= Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE? false:true;

    }
    private void initNextNavigation(){
        long totalPageCount=(long)Math.ceil(this.curPage/(double)Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE)*Pagination.DEFAULT_PAGE_PER_NAVIGATION_SIZE;
        this.nextNavigation=totalPageCount>=this.totalPages? false:true;
    }
}

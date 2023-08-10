package com.reminiscence.article.framearticle.dto;

import com.reminiscence.article.framearticle.vo.FrameArticleVo;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import com.reminiscence.article.util.PageNavigator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class FrameArticleListResponseDto implements Serializable {

    private PageNavigator pageNavigator;
    List<FrameArticleVo> frameArticleVoList;

    public FrameArticleListResponseDto(int curPage, int totalPages, long totalElements) {
        this.pageNavigator=new PageNavigator(curPage,totalPages,totalElements);
        this.frameArticleVoList=new ArrayList<>();
    }

    public void add(FrameArticleVo frameArticleVo){
        this.frameArticleVoList.add(frameArticleVo);
    }

}

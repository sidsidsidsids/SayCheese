package com.reminiscence.article.image.dto;


import com.reminiscence.article.image.vo.ImageVo;
import com.reminiscence.article.util.PageNavigator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OwnerImageListResponseDto implements Serializable {

    private PageNavigator pageNavigator;
    List<ImageVo> imageVoList;

    public OwnerImageListResponseDto(int curPage, int totalPages, long totalElements, List<ImageVo> imageVoList) {
        this.pageNavigator=new PageNavigator(curPage,totalPages,totalElements);
        this.imageVoList=imageVoList;
    }
}

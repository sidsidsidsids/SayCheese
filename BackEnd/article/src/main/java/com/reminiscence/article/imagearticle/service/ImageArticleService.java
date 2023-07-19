package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageRequestDto;

public interface ImageArticleService {
   public int writeImageArticle(Image image, Long memberId);
   public ImageArticleDetailResponseDto getImageArticleDetail(Long articleId);


}

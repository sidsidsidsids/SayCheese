package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.imagearticle.dto.DeleteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface ImageArticleService {
   ImageArticleDetailResponseDto getImageArticleDetail(Long articleId);
   @Transactional
   void writeImageArticle(Long image, Long memberId);
   @Transactional
   void deleteImageArticle(Long articleId);

   @Transactional
   void deleteImageAndArticle(Long articleId);




}

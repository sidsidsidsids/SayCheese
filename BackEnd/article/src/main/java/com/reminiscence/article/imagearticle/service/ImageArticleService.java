package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.imagearticle.dto.DeleteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageArticleService {
   ImageArticleDetailResponseDto getImageArticleDetail(Long articleId);

   List<ImageArticleListResponseDto> getHotImageArticleList();
   List<ImageArticleListResponseDto> getTagImageArticleList(Long tagId);
   List<ImageArticleListResponseDto> getRandomTagImageArticleList();
   List<ImageArticleListResponseDto> getRecentImageArticleList();
   List<ImageArticleListResponseDto> getRandomImageArticleList();
   @Transactional
   void writeImageArticle(Long image, Long memberId);
   @Transactional
   void deleteImageArticle(Long memberId, Long articleId);
   @Transactional
   void deleteImageAndArticle(Long memberId, Long articleId);




}

package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.imagearticle.dto.DeleteImageArticleRequestDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageArticleService {
   ImageArticleDetailResponseDto getImageArticleDetail(Long articleId,UserDetail userDetail);

   List<ImageArticleListResponseDto> getHotImageArticleList(UserDetail userDetail);
   List<ImageArticleListResponseDto> getTagImageArticleList(Long tagId, UserDetail userDetail);
   List<ImageArticleListResponseDto> getRandomTagImageArticleList(UserDetail userDetail);
   List<ImageArticleListResponseDto> getRecentImageArticleList(UserDetail userDetail);
   List<ImageArticleListResponseDto> getRandomImageArticleList(UserDetail userDetail);
   @Transactional
   void writeImageArticle(Long imageId, Long memberId);
   @Transactional
   void deleteImageArticle(Long memberId, Long articleId);
   @Transactional
   void deleteImageAndArticle(Long memberId, Long articleId);




}

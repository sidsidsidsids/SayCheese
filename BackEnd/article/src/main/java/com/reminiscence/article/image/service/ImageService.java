package com.reminiscence.article.image.service;

import com.reminiscence.article.domain.Image;

public interface ImageService {
   public void saveImage(Image image);
   public int writeImageArticle(Image image, Long memberId);
}

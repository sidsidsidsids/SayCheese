package com.reminiscence.article.image.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.image.dto.RandomTagResponseDto;

import java.util.List;

public interface ImageService {
    List<OwnerImageResponseDto> getReadRecentOwnImages(Long memberId);
    List<RandomTagResponseDto> getRandomTags();
    void saveImage(UserDetail userDetail, String fileName, String imageLink);
//    public void readImageOwner(Long id);
    void deleteImage( Long imageId, Long memberId);
}

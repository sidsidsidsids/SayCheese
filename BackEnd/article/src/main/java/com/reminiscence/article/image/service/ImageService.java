package com.reminiscence.article.image.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.ImageWriteRequestDto;
import com.reminiscence.article.image.dto.ImageWriteResponseDto;
import com.reminiscence.article.image.dto.OwnerImageListResponseDto;
import com.reminiscence.article.image.dto.RandomTagResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ImageService {
    OwnerImageListResponseDto getReadRecentOwnImages(Long memberId, Pageable requestPageable);
    List<RandomTagResponseDto> getRandomTags();
    ImageWriteResponseDto saveImage(UserDetail userDetail, ImageWriteRequestDto requestDto);
    void deleteImage( Long imageId, Long memberId);
}

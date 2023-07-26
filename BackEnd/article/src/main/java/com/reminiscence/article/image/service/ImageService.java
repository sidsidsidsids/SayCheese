package com.reminiscence.article.image.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.image.dto.PreSignedResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ImageService {
    void saveImage(UserDetail userDetail, String fileName, String imageLink);
    List<OwnerImageResponseDto> getReadRecentOwnImages(Long memberId);
//    public void readImageOwner(Long id);
    PreSignedResponseDto getPreSignedUrl(String prefix, String fileName);
    @Transactional
    void deleteImage( Long imageId, Long memberId);
}

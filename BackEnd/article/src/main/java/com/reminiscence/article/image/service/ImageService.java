package com.reminiscence.article.image.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.image.dto.PreSignedResponseDto;

public interface ImageService {
    public void saveImage(UserDetail userDetail, String fileName, String imageLink);
//    public void readOwnImages(UserDetail userDetail);
//    public void readImageOwner(Long id);
    PreSignedResponseDto getPreSignedUrl(String prefix, String fileName);
}

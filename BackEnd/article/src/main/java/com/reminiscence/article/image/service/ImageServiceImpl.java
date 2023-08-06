package com.reminiscence.article.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageOwner;
import com.reminiscence.article.exception.customexception.ImageException;
import com.reminiscence.article.exception.message.ImageExceptionMessage;
import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.image.dto.RandomTagResponseDto;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final ImageOwnerRepository imageOwnerRepository;
    private final TagRepository tagRepository;

    @Override
    public List<OwnerImageResponseDto> getReadRecentOwnImages(Long memberId) {
        return null;
    }

    @Override
    public List<RandomTagResponseDto> getRandomTags() {
        Optional<List<RandomTagResponseDto>> randomTags = tagRepository.findRandomTags();
        randomTags.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_RANDOM_TAGS));
        return randomTags.get();
    }

    // 이미지 소유자 저장 기능 추가 필요!!
    // 방 참여자 로직이 정해지지 않아서 나중에 추가해야 한다.
    @Override
    public void saveImage(UserDetail userDetail, String fileName, String imageLink) {
        String fileType = getFileType(fileName);
        String name = getFileName(fileName);
        Image image = Image.builder()
                .link(imageLink)
                .type(fileType)
                .name(name)
                .build();
        imageRepository.save(image);
    }

    @Transactional
    @Override
    public void deleteImage(Long imageId, Long memberId) {
        Optional<ImageOwner> findImage = imageOwnerRepository.findByImageIdAndMemberId(imageId, memberId);

        findImage.orElseThrow(()-> new ImageException(ImageExceptionMessage.NOT_FOUND_IMAGE_OWNER));

        imageOwnerRepository.delete(findImage.get());
    }

    private String getFileType(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
    private String getFileName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
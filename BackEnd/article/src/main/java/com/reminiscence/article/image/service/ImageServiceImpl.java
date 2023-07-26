package com.reminiscence.article.image.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageOwner;
import com.reminiscence.article.exception.customexception.ImageException;
import com.reminiscence.article.exception.message.ImageExceptionMessage;
import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.image.dto.PreSignedResponseDto;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService{
    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private final ImageOwnerRepository imageOwnerRepository;

    @Override
    public List<OwnerImageResponseDto> getReadRecentOwnImages(Long memberId) {
        return null;
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

    public PreSignedResponseDto getPreSignedUrl(String prefix, String fileName) {
        String bucket = "200oks3bucket";
        String oneFileName = onlyOneFileName(fileName);
        if (!prefix.equals("")) {
            fileName = prefix + "/" + oneFileName;
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(bucket, fileName);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

        return new PreSignedResponseDto(url.toString(), oneFileName);
    }

    private GeneratePresignedUrlRequest getGeneratePreSignedUrlRequest(String bucket, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, fileName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(getPreSignedUrlExpiration());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL,
                CannedAccessControlList.PublicRead.toString());
        return generatePresignedUrlRequest;
    }

    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private String onlyOneFileName(String filename){
        return UUID.randomUUID().toString()+filename;
    }
    private String getFileType(String fileName){
        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
    private String getFileName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

}
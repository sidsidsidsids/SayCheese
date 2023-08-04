package com.reminiscence.article.frame.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.framearticle.dto.FrameArticleRequestDto;
import com.reminiscence.article.frame.dto.PreSignedResponseDto;
import com.reminiscence.article.frame.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FrameServiceImpl implements FrameService {
    private final FrameRepository frameRepository;
    private final AmazonS3 amazonS3;

    @Override
    public PreSignedResponseDto getPreSignedUrl(String bucket, String prefix, String fileName) {
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

}

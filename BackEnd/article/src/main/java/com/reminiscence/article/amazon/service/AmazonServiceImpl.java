package com.reminiscence.article.amazon.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.reminiscence.article.amazon.dto.PreSignedResponseDto;
import com.reminiscence.article.frame.repository.FrameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AmazonServiceImpl implements AmazonService {

    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    @Override
    public PreSignedResponseDto getPreSignedUrl(String prefix, String fileName) {
        String oneFileName = onlyOneFileName(fileName);
        if (!prefix.equals("")) {

            fileName = prefix + "/" + oneFileName;
        }
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePreSignedUrlRequest(BUCKET_NAME, fileName);
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

        generatePresignedUrlRequest.addRequestParameter(
                Headers.CONTENT_TYPE, "image/png");
        return generatePresignedUrlRequest;
    }
    private Date getPreSignedUrlExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 2;
        expiration.setTime(expTimeMillis);
        return expiration;
    }
    private String onlyOneFileName(String fileName){
        return UUID.randomUUID().toString() + "." + fileName.substring(fileName.lastIndexOf(".")+1);
    }

}

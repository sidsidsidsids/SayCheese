package com.reminiscence.article.schedule;

import com.amazonaws.services.s3.AmazonS3;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageDeleteSchedule {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;
    private String bucket = "200oks3bucket";
    private String prefix = "image";

    @Scheduled(cron = "0 0 2 * * *")
    public void deleteImage(){
        List<ImageDeleteResponseDto> imageList = imageRepository.findNonOwnerImage().orElse(null);
        if(imageList == null) {
            return;
        }
        List<Long> imageIdList = new ArrayList<>();
        for(ImageDeleteResponseDto images : imageList){
            deleteFile(images.getImageName(), images.getImageType());
            imageIdList.add(images.getImageId());
        }
        imageRepository.deleteNonOwnerImage(imageIdList);
    }

    public void deleteFile(String fileName, String imageType){
        String keyName = prefix + "/" + fileName + "." + imageType;
        boolean isObjectExist = amazonS3.doesObjectExist(bucket, keyName);
        if(!isObjectExist) {
            return;
        }
        amazonS3.deleteObject(bucket, keyName);
    }
}

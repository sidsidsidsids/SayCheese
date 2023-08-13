package com.reminiscence.article.image.service;

import com.reminiscence.article.common.Pagination;
import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.*;
import com.reminiscence.article.exception.customexception.ImageException;
import com.reminiscence.article.exception.message.ImageExceptionMessage;
import com.reminiscence.article.image.dto.*;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.ImageTagRepository;
import com.reminiscence.article.image.repository.TagRepository;
import com.reminiscence.article.image.vo.ImageVo;
import com.reminiscence.article.notice.dto.NoticeArticleListResponseDto;
import com.reminiscence.article.notice.vo.NoticeArticleVo;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final ImageOwnerRepository imageOwnerRepository;
    private final TagRepository tagRepository;
    private final ImageTagRepository imageTagRepository;
    private final WebClient webClient;
    private final JdbcTemplate jdbcTemplate;
    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    @Value("${cloud.aws.region.static}")
    private String BUCKET_REGION;



    @Override
    public OwnerImageListResponseDto getReadRecentOwnImages(Long memberId, Pageable requestPageable) {
        int page = requestPageable.getPageNumber();
        if(page<=0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page-1, Pagination.DEFAULT_OWN_IMAGE_PER_PAGE_SIZE, Sort.Direction.DESC,"id");
        Optional<Page<ImageVo>> images = imageRepository.findRecentOwnerImage(memberId, pageable);
        images.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_IMAGE));

        OwnerImageListResponseDto ownerImageListResponseDto = new OwnerImageListResponseDto(page, images.get().getTotalPages(), images.get().getTotalElements(), images.get().getContent());
        
        return ownerImageListResponseDto;
    }

    @Override
    public List<RandomTagResponseDto> getRandomTags() {
        Optional<List<RandomTagResponseDto>> randomTags = tagRepository.findRandomTags();
        randomTags.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_RANDOM_TAGS));
        return randomTags.get();
    }

    @Override
    public ImageWriteResponseDto saveImage(UserDetail userDetail, ImageWriteRequestDto requestDto) {
        StringBuilder imageLink = new StringBuilder();
         imageLink.append("https://")
                 .append(BUCKET_NAME)
                 .append(".s3.")
                 .append(BUCKET_REGION)
                 .append(".amazonaws.com/")
                 .append(requestDto.getFileType().getValue())
                 .append("/")
                 .append(requestDto.getImageName());
        String imageType = getFileType(requestDto.getImageName());
        String name = getFileName(requestDto.getImageName());
        Image image = Image.builder()
                .link(imageLink.toString())
                .type(imageType)
                .name(name)
                .build();
        imageRepository.save(image);
        List<ImageTag> imageTags = new ArrayList<>();
        for(Long tagId : requestDto.getTags()){
            imageTags.add(new ImageTag(image,tagId));
        }
        String imageSql = "insert into image_tag(image_id, tag_id) values(?,?)";
        jdbcTemplate.batchUpdate(imageSql,
                imageTags,
                imageTags.size(),
                (ps, imageTag)->{
            ps.setLong(1,imageTag.getImageTagKey().getImageId());
            ps.setLong(2,imageTag.getImageTagKey().getTagId());
        });
        Mono<RoomParticipantDto[]> participants = webClient.get()
                .uri("/api/participant/" + requestDto.getRoomCode())
                .retrieve()
                .bodyToMono(RoomParticipantDto[].class);


        participants.subscribe(
                response->{
                    String imageOwnerSql = "insert into image_owner(image_id, member_id) values(?,?)";
                    List<RoomParticipantDto> list = Collections.arrayToList(response);
                    jdbcTemplate.batchUpdate(imageOwnerSql, list, list.size(),
                    (ps, memberId)->{
                    ps.setLong(1,image.getId());
                    ps.setLong(2, memberId.getMemberId());
                });
                }
        );
        return new ImageWriteResponseDto(image.getId());
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
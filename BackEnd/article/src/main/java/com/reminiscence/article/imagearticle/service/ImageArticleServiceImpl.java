package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.exception.customexception.ImageArticleException;
import com.reminiscence.article.exception.customexception.ImageException;
import com.reminiscence.article.exception.message.ImageArticleExceptionMessage;
import com.reminiscence.article.exception.message.ImageExceptionMessage;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.ImageTagRepository;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageArticleServiceImpl implements ImageArticleService {

    private final ImageArticleRepository imageArticleRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageTagRepository imageTagRepository;

    @Override
    public ImageArticleDetailResponseDto getImageArticleDetail(Long articleId){
        Optional<ImageArticle> findImageArticle = imageArticleRepository.findImageArticleOfAllById(articleId);
        findImageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));

        return new ImageArticleDetailResponseDto(findImageArticle.get());

    }

    @Transactional
    @Override
    public void writeImageArticle(Long image, Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Optional<Image> findImage = imageRepository.findById(image);

        findImage.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_IMAGE));

        ImageArticle imageArticle = ImageArticle.builder()
                .member(findMember.get())
                .image(findImage.get())
                .build();

        imageArticleRepository.save(imageArticle);
    }
    @Override
    public void deleteImageArticle(Long articleId) {
        Optional<ImageArticle> imageArticle = imageArticleRepository.findImageArticleOfMemberById(articleId);
        imageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));
        imageArticleRepository.delete(imageArticle.get());
    }
    @Override
    public void deleteImageAndArticle(Long articleId) {
        Optional<ImageArticle> imageArticle = imageArticleRepository.findImageArticleOfAllById(articleId);
        imageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));
        imageArticleRepository.delete(imageArticle.get());
        imageRepository.delete(imageArticle.get().getImage());
        imageTagRepository.deleteByImageId(imageArticle.get().getImage().getId());
    }

}

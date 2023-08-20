package com.reminiscence.article.imagearticle.service;

import com.reminiscence.article.config.auth.UserDetail;
import com.reminiscence.article.domain.Image;
import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.domain.Tag;
import com.reminiscence.article.exception.customexception.ImageArticleException;
import com.reminiscence.article.exception.customexception.ImageException;
import com.reminiscence.article.exception.customexception.MemberException;
import com.reminiscence.article.exception.message.ImageArticleExceptionMessage;
import com.reminiscence.article.exception.message.ImageExceptionMessage;
import com.reminiscence.article.exception.message.MemberExceptionMessage;
import com.reminiscence.article.image.repository.ImageOwnerRepository;
import com.reminiscence.article.image.repository.ImageRepository;
import com.reminiscence.article.image.repository.ImageTagRepository;
import com.reminiscence.article.image.repository.TagRepository;
import com.reminiscence.article.imagearticle.dto.ImageArticleDetailResponseDto;
import com.reminiscence.article.imagearticle.dto.ImageArticleListResponseDto;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import com.reminiscence.article.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageArticleServiceImpl implements ImageArticleService {

    private final ImageArticleRepository imageArticleRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageTagRepository imageTagRepository;
    private final TagRepository tagRepository;
    private final LoverRepository loverRepository;
    private final ImageOwnerRepository imageOwnerRepository;


    @Override
    public List<ImageArticleListResponseDto> getHotImageArticleList(UserDetail userDetail) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        if(userDetail != null){
            return getMemberImageArticleList(page, userDetail.getMember().getId());
        }else{
            return getNonMemberImageArticleList(page);
        }
    }

    @Override
    public List<ImageArticleListResponseDto> getTagImageArticleList(Long tagId, UserDetail userDetail) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "lover");
        if(userDetail != null) {
            return getMemberTagImageArticleList(page, tagId, userDetail.getMember().getId());
        }else{
            return getNonTagImageArticleList(page, tagId);
        }
    }

    @Override
    public List<ImageArticleListResponseDto> getRandomTagImageArticleList(UserDetail userDetail) {
        Optional<Tag> randomTag = tagRepository.findRandomTag();
        randomTag.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_TAG));

        Long tagId = randomTag.get().getId();
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "random");
        if(userDetail != null) {
            return getMemberTagImageArticleList(page, tagId, userDetail.getMember().getId());
        }else{
            return getNonTagImageArticleList(page, tagId);
        }
    }

    @Override
    public List<ImageArticleListResponseDto> getRecentImageArticleList(UserDetail userDetail) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "createdDate");
        List<ImageArticleListResponseDto> imageArticleList;
        if(userDetail != null){
            imageArticleList = getMemberImageArticleList(page, userDetail.getMember().getId());
        }else{
            imageArticleList = getNonMemberImageArticleList(page);
        }
        return imageArticleList;
    }

    @Override
    public List<ImageArticleListResponseDto> getRandomImageArticleList(UserDetail userDetail) {
        PageRequest page = PageRequest.of(0, 10, Sort.Direction.DESC, "random");
        List<ImageArticleListResponseDto> imageArticleList;
        if(userDetail != null){
            imageArticleList = getMemberImageArticleList(page, userDetail.getMember().getId());
        }else{
            imageArticleList = getNonMemberImageArticleList(page);
        }
        return imageArticleList;
    }

    @Override
    public ImageArticleDetailResponseDto getImageArticleDetail(Long articleId,UserDetail userDetail){
        Optional<ImageArticleDetailResponseDto> findImageArticle;
        if(userDetail != null){
            findImageArticle = imageArticleRepository.findMemberImageArticleDetailById(articleId, userDetail.getMember().getId());
        }else{
            findImageArticle = imageArticleRepository.findNonMemberImageArticleDetailById(articleId);
        }
        findImageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));

        Optional<List<String>> findTags = imageTagRepository.findTagNameByImageId(findImageArticle.get().getImageId());
        findTags.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_IMAGE_TAG));

        findImageArticle.get().setTags(findTags.get());
        return findImageArticle.get();
    }

    @Transactional
    @Override
    public void writeImageArticle(Long imageId, Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        Optional<Image> findImage = imageRepository.findById(imageId);

        findMember.orElseThrow(()->
                new MemberException(MemberExceptionMessage.NOT_FOUND_MEMBER));

        findImage.orElseThrow(()->
                new ImageException(ImageExceptionMessage.NOT_FOUND_IMAGE));

        ImageArticle imageArticle = ImageArticle.builder()
                .member(findMember.get())
                .image(findImage.get())
                .build();

        imageArticleRepository.save(imageArticle);
    }
    @Transactional
    @Override
    public void deleteImageArticle(Long memberId,Long articleId) {
        Optional<ImageArticle> imageArticle = imageArticleRepository.findImageArticleOfMemberById(articleId);
        imageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));
        userConfirm(memberId, imageArticle);
        loverRepository.deleteByArticleId(articleId);
        imageArticleRepository.delete(imageArticle.get());
    }
    @Transactional
    @Override
    public void deleteImageAndArticle(Long memberId, Long articleId) {
        Optional<ImageArticle> imageArticle = imageArticleRepository.findImageArticleOfAllById(articleId);
        imageArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));
        userConfirm(memberId, imageArticle);
        loverRepository.deleteByArticleId(articleId);
        imageOwnerRepository.deleteByImageIdAndMemberId(imageArticle.get().getImage().getId(), imageArticle.get().getMember().getId());
        imageArticleRepository.delete(imageArticle.get());
    }

    private void userConfirm(Long memberId, Optional<ImageArticle> imageArticle) {
        if (memberId != imageArticle.get().getMember().getId()) {
            throw new MemberException(MemberExceptionMessage.NOT_MATCH_MEMBER);
        }
    }

    private List<ImageArticleListResponseDto> getMemberImageArticleList(Pageable page, Long memberId){
        Optional<List<ImageArticleListResponseDto>> imageArticles = imageArticleRepository.findMemberCommonImageArticles(page, memberId);
        imageArticles.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE_LIST));
        return imageArticles.get();
    }
    private List<ImageArticleListResponseDto> getNonMemberImageArticleList(Pageable page){
        Optional<List<ImageArticleListResponseDto>> imageArticles = imageArticleRepository.findNonMemberCommonImageArticles(page);
        imageArticles.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE_LIST));
        return imageArticles.get();
    }

    private List<ImageArticleListResponseDto> getMemberTagImageArticleList(Pageable page , Long tagId, Long memberId){
        Optional<List<ImageArticleListResponseDto>> imageArticles = imageArticleRepository.findMemberTagImageArticles(tagId,page, memberId);
        imageArticles.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE_LIST));
        return imageArticles.get();
    }
    private List<ImageArticleListResponseDto> getNonTagImageArticleList(Pageable page , Long tagId){
        Optional<List<ImageArticleListResponseDto>> imageArticles = imageArticleRepository.findNonMemberTagImageArticles(tagId,page);
        imageArticles.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE_LIST));
        return imageArticles.get();
    }
}

package com.reminiscence.article.lover.service;

import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.Lover;
import com.reminiscence.article.exception.customexception.ImageArticleException;
import com.reminiscence.article.exception.customexception.LoverException;
import com.reminiscence.article.exception.message.ImageArticleExceptionMessage;
import com.reminiscence.article.exception.message.LoverExceptionMessage;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoverServiceImpl implements LoverService{

    private final LoverRepository loverRepository;
    private final ImageArticleRepository imageArticleRepository;

    @Override
    public boolean writeLoverImageArticle(Long memberId, Long articleId) {

        Optional<ImageArticle> findArticle = imageArticleRepository.findById(articleId);
        findArticle.orElseThrow(()->
                new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_IMAGE_ARTICLE));
        Lover lover = Lover.builder()
                .memberId(memberId)
                .article(findArticle.get())
                .build();
        loverRepository.save(lover);
        return true;
    }

    @Override
    public boolean deleteLoverImageArticle(Long memberId, Long articleId) {
        Optional<Lover> findLover = loverRepository.findByMemberIdAndArticleId(memberId, articleId);
        findLover.orElseThrow(()->
                new LoverException(LoverExceptionMessage.NOT_FOUND_LOVER));
        loverRepository.delete(findLover.get());
        return true;
    }

}

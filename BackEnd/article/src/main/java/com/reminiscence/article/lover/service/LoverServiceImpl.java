package com.reminiscence.article.lover.service;

import com.reminiscence.article.domain.*;
import com.reminiscence.article.exception.customexception.FrameArticleException;
import com.reminiscence.article.exception.customexception.ImageArticleException;
import com.reminiscence.article.exception.customexception.LoverException;
import com.reminiscence.article.exception.message.FrameArticleExceptionMessage;
import com.reminiscence.article.exception.message.ImageArticleExceptionMessage;
import com.reminiscence.article.exception.message.LoverExceptionMessage;
import com.reminiscence.article.frame.repository.FrameRepository;
import com.reminiscence.article.framearticle.repository.FrameArticleRepository;
import com.reminiscence.article.imagearticle.repository.ImageArticleRepository;
import com.reminiscence.article.lover.repository.LoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoverServiceImpl implements LoverService{

    private final LoverRepository loverRepository;
    private final ImageArticleRepository imageArticleRepository;
    private final FrameArticleRepository frameArticleRepository;

    @Transactional
    @Override
    public void writeLoverArticle(Long memberId, Long articleId, String articleType) {
        Lover lover = Lover.builder()
                .memberId(memberId)
                .build();
        if(articleType.equals("image")){
            Optional<ImageArticle> findArticle = imageArticleRepository.findById(articleId);
            findArticle.orElseThrow(()->
                    new ImageArticleException(ImageArticleExceptionMessage.NOT_FOUND_ARTICLE));
            lover.setArticle(findArticle.get());
        }else if(articleType.equals("frame")){
            Optional<FrameArticle> findArticle = frameArticleRepository.findById(articleId);
            findArticle.orElseThrow(()->
                    new FrameArticleException(FrameArticleExceptionMessage.NOT_FOUND_FRAME_ARTICLE));
            lover.setArticle(findArticle.get());
        }else{
            throw new LoverException(LoverExceptionMessage.NOT_FOUND_ARTICLE_TYPE);
        }

        loverRepository.save(lover);
    }

    @Transactional
    @Override
    public void deleteLoverArticle(Long memberId, Long articleId) {
        Optional<Lover> findLover = loverRepository.findByMemberIdAndArticleId(memberId, articleId);
        findLover.orElseThrow(()->
                new LoverException(LoverExceptionMessage.NOT_FOUND_LOVER));
        loverRepository.delete(findLover.get());
    }

}

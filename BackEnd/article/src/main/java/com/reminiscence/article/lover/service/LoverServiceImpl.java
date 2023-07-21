package com.reminiscence.article.lover.service;

import com.reminiscence.article.domain.Article;
import com.reminiscence.article.domain.ImageArticle;
import com.reminiscence.article.domain.Lover;
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
        if(!imageArticleValidate(findArticle)){
            return false;
        }
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
        if(!loverValidate(findLover)){
            return false;
        }
        loverRepository.delete(findLover.get());
        return true;
    }

    public boolean imageArticleValidate(Optional<ImageArticle> article){
        return article.isPresent();
    }
    public boolean loverValidate(Optional<Lover> lover){
        return lover.isPresent();
    }

}

package com.reminiscence.article.lover.service;

public interface LoverService {

    public boolean writeLoverImageArticle(Long memberId, Long articleId);
    public boolean deleteLoverImageArticle(Long memberId, Long articleId);
}

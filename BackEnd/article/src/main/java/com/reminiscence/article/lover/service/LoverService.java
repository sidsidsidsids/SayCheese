package com.reminiscence.article.lover.service;

public interface LoverService {

    public void writeLoverArticle(Long memberId, Long articleId);
    public void deleteLoverArticle(Long memberId, Long articleId);
}

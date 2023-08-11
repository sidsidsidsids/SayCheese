package com.reminiscence.article.lover.service;

public interface LoverService {

    public void writeLoverArticle(Long memberId, Long articleId, String articleType);
    public void deleteLoverArticle(Long memberId, Long articleId);
}

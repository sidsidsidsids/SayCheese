package com.reminiscence.article.notice.repository;

import com.reminiscence.article.domain.NoticeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeRepositoryCustom {
    Page<NoticeArticle> findNoticeArticle(Pageable pageable);

    NoticeArticle findNoticeArticleById(Long id);
}

package com.reminiscence.article.notice.repository;

import com.reminiscence.article.domain.NoticeArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NoticeRepositoryCustom {
    Page<NoticeArticle> findNoticeArticle(Pageable pageable);

    Optional<NoticeArticle> findNoticeArticleById(Long id);
}

package com.reminiscence.article.notice.repository;

import com.reminiscence.article.domain.NoticeArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<NoticeArticle,Long>, NoticeRepositoryCustom {
    Optional<NoticeArticle> findById(Long id);
}

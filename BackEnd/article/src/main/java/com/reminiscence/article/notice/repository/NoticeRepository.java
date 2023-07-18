package com.reminiscence.article.notice.repository;

import com.reminiscence.article.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
}

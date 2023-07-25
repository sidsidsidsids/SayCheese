package com.reminiscence.article.frame.repository;

import com.reminiscence.article.domain.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameRepository extends JpaRepository<Frame,Long> {
}

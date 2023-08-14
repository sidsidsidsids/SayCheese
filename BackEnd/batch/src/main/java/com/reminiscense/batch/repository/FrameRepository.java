package com.reminiscense.batch.repository;

import com.reminiscense.batch.domain.Frame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrameRepository extends JpaRepository<Frame,Long>, FrameRepositoryCustom {
}

package com.reminiscense.batch.repository;

import com.reminiscense.batch.domain.Frame;

import java.util.List;

public interface FrameRepositoryCustom {
    List<Frame> findByDeletedFrame();
    void deleteFrames(List<Frame> frameList);
}

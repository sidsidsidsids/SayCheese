package com.reminiscence.article.image.repository;

import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;

import java.util.List;
import java.util.Optional;

public interface ImageRepositoryCustom {
    Optional<List<ImageDeleteResponseDto>> findNonOwnerImage();
}

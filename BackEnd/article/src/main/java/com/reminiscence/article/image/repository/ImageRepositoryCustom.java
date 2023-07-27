package com.reminiscence.article.image.repository;

import com.reminiscence.article.image.dto.OwnerImageResponseDto;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ImageRepositoryCustom {
    Optional<List<ImageDeleteResponseDto>> findNonOwnerImage();
    Optional<List<OwnerImageResponseDto>> findRecentOwnerImage(Pageable page, Long memberId);
}

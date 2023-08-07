package com.reminiscence.article.image.repository;

import com.reminiscence.article.image.dto.RandomTagResponseDto;

import java.util.List;
import java.util.Optional;

public interface TagRepositoryCustom {
    Optional<List<RandomTagResponseDto>> findRandomTags();
}

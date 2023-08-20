package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.Image;
import com.reminiscence.article.image.vo.ImageVo;
import com.reminiscence.article.schedule.dto.ImageDeleteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ImageRepositoryCustom {
    Optional<List<ImageDeleteResponseDto>> findNonOwnerImage();
    Optional<Page<ImageVo>> findRecentOwnerImage(Long memberId, Pageable page);
}

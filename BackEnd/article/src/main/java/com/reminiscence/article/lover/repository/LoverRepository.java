package com.reminiscence.article.lover.repository;

import com.reminiscence.article.domain.Lover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoverRepository extends JpaRepository<Lover, Long> {
    public void deleteByArticleId(Long articleId);
    public Optional<List<Lover>> findByArticleId(Long articleId);
    public Optional<Lover> findByMemberIdAndArticleId(Long memberId, Long articleId);
}

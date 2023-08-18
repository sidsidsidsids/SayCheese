package com.reminiscence.article.image.repository;

import com.reminiscence.article.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, TagRepositoryCustom {
    @Query(value = "select * from Tag order by rand() limit 1", nativeQuery = true)
    public Optional<Tag> findRandomTag();
}

package com.reminiscence.article.qna.repository;

import com.reminiscence.article.domain.FrequentlyAskedQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<FrequentlyAskedQuestion,Long> {

}

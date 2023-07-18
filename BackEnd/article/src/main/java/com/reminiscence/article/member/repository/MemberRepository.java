package com.reminiscence.article.member.repository;


import com.reminiscence.article.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findById(Long id);
}

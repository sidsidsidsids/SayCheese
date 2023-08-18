package com.reminiscence.member.repository;


import com.reminiscence.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findById(Long id);

    //    Member findByEmailAndPassword(String email, String password) throws SQLException;
    Member findByEmail(String email) throws SQLException;

    Member findByNickname(String nickname) throws SQLException;

    List<Member> findAllByEmail(String memberId);

}

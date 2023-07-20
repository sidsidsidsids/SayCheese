package com.reminiscence.member.repository;


import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.MemberResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT email, password, nickname, role_type, genderfm, age, name, profile, sns_id, sns_type, personal_agreement from member where email =:email and password =:password", nativeQuery = true)
    MemberResponseDto login(@Param("email") String email, @Param("password") String password) throws SQLException;
    Member findByEmail(String email) throws SQLException;
    Member findByNickname(String nickname) throws SQLException;
    Member deleteByEmail(String memberId) throws  SQLException;
    @Query(value = "SELECT nickname, email from member where email = :key or nickname = :key", nativeQuery = true)
    List<Member> getMemberList(@Param("key") String key) throws SQLException;
    @Query(value = "SELECT token from member where member_id = :memberId", nativeQuery = true)
    Object getRefreshToken(@Param("memberId") String memberId) throws SQLException;
    @Query(value = "update member set token =:token where member_id = :memberId ", nativeQuery = true)
    void saveRefreshToken(@Param("token") String token, @Param("memberId") String memberId) throws SQLException;
    @Query(value = "update member set token =:token where member_id = :memberId", nativeQuery = true)
    void deleteRefreshToken(@Param("token") String token, @Param("memberId") String memberId) throws SQLException;
}

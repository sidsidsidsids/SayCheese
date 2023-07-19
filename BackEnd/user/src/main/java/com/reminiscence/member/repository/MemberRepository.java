package com.reminiscence.member.repository;


import com.reminiscence.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.SQLException;
import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(value = "SELECT * from member where email = :key or nickname = :key", nativeQuery = true)
    List<Member> getMemberList(String key) throws SQLException;

//    int idCheck(String memberId) throws SQLException;
//    void join(Member member) throws SQLException;
//
//    //	MemberDto login(Map<String, String> map) throws SQLException;
//    Member login(Member member) throws SQLException;
//    //	MemberDto getMemberInfo(String memberId) throws SQLException;
//    Member memberInfo(String memberid) throws SQLException;
//    void updatePw(Map<String, String> map) throws SQLException;
//    void modify(Member member) throws SQLException;
//    void delete(String memberId) throws SQLException;
//
//    Member findID(String memberId) throws SQLException;
//    void saveRefreshToken(Map<String, String> map) throws SQLException;
//    Object getRefreshToken(String memberid) throws SQLException;
//    void deleteRefreshToken(Map<String, String> map) throws SQLException;
}

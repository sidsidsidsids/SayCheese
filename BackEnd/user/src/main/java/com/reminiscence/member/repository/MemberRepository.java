package com.reminiscence.member.repository;


import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.MemberResponseDto;
import com.reminiscence.member.dto.MemberSearchResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findById(Long id);

    //    Member findByEmailAndPassword(String email, String password) throws SQLException;
    Member findByEmail(String email) throws SQLException;

    Member findByNickname(String nickname) throws SQLException;

    List<Member> findAllByEmail(String memberId);

//    List<Member> findAllByEmailOrNickname(String email) throws SQLException;

    //    List<Member> findAllByEmailContainingIgnoreCaseOrNicknameContainingIgnoreCase(String emailNickname);

    //    @Query(value = "SELECT * from Member where email like CONCAT('%', :key, '%') or nickname like CONCAT('%', :key, '%')", nativeQuery = true)
//    @Query("SELECT new com.reminiscence.member.dto.MemberSearchResponseDto(m.email, m.nickname) FROM Member m WHERE m.email LIKE CONCAT('%', :key, '%') OR m.nickname LIKE CONCAT('%', :key, '%')")
//    List<MemberSearchResponseDto> getMemberList(@Param("key") String key) throws SQLException;
//    @Query(value = "SELECT token from Member where member_id = :memberId", nativeQuery = true)
//    Object getRefreshToken(@Param("memberId") String memberId) throws SQLException;
//    @Query(value = "update Member set token =:token where member_id = :memberId ", nativeQuery = true)
//    void saveRefreshToken(@Param("token") String token, @Param("memberId") String memberId) throws SQLException;
//    @Query(value = "update Member set token =:token where member_id = :memberId", nativeQuery = true)
//    void deleteRefreshToken(@Param("token") String token, @Param("memberId") String memberId) throws SQLException;
}

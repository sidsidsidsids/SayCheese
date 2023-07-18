package com.reminiscence.service.member;

import java.util.Map;

import com.reminiscence.model.member.Member;

public interface MemberService {

    int idCheck(String memberId) throws Exception;
    void join(Member member) throws Exception;
    //	MemberDto login(Map<String, String> map) throws Exception;
    Member login(Member member) throws Exception;
    void updatePw(Map<String, String> map) throws Exception;
    /* Admin */
//	List<MemberDto> listMember(Map<String, Object> map) throws Exception;
//	MemberDto getMember(String memberId) throws Exception;
    Member memberInfo(String memberid) throws Exception;
    void updateMember(Member member) throws Exception;
    void deleteMember(String memberid) throws Exception;

    void saveRefreshToken(String memberid, String refreshToken) throws Exception;
    Object getRefreshToken(String memberid) throws Exception;
    void deleRefreshToken(String memberid) throws Exception;
}

package com.reminiscence.member.service;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.MemberJoinRequestDto;
import com.reminiscence.domain.MemberUpdatePasswordDto;
import com.reminiscence.domain.MemberUpdateRequestDto;

import java.util.List;

public interface MemberService {

//    int idCheck(String memberId) throws Exception;
    Member joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception;
//    MemberDto login(Map<String, String> map) throws Exception;
//    Member login(Member member) throws Exception;
//    void updatePw(Map<String, String> map) throws Exception;
//    /* Admin */
	List<Member> getMemberList(String key) throws Exception;
////	MemberDto getMember(String memberId) throws Exception;
    Member getMemberInfo(long memberid) throws Exception;
    Member updateMemberPassword(MemberUpdatePasswordDto memberUpdatePasswordDto) throws Exception;
    Member updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto) throws  Exception;
//    void updateMember(Member member) throws Exception;
//    void deleteMember(String memberid) throws Exception;
//
//    void saveRefreshToken(String memberid, String refreshToken) throws Exception;
//    Object getRefreshToken(String memberid) throws Exception;
//    void deleRefreshToken(String memberid) throws Exception;
}

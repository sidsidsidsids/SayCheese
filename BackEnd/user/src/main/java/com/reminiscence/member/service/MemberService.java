package com.reminiscence.member.service;

import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.MemberJoinRequestDto;
import com.reminiscence.member.dto.MemberLoginRequestDto;
import com.reminiscence.member.dto.MemberUpdatePasswordRequestDto;
import com.reminiscence.member.dto.MemberInfoUpdateRequestDto;

import java.util.List;

public interface MemberService {

    Member login(MemberLoginRequestDto memberLoginRequestDto) throws Exception;
    Member joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception;
    Member emailCheck(String email) throws Exception;
    Member nicknameCheck(String nickname) throws Exception;
//    /* Admin */
	List<Member> getMemberList(String key) throws Exception;
    Member getMemberInfo(String memberId) throws Exception;
    Member updateMemberPassword(MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception;
    Member updateMemberInfo(MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) throws  Exception;

    void deleteMember(String memberId) throws Exception;
    void saveRefreshToken(String memberid, String refreshToken) throws Exception;
    Object getRefreshToken(String memberid) throws Exception;
    void deleteRefreshToken(String memberid) throws Exception;
}

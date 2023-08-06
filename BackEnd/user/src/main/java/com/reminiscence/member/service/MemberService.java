package com.reminiscence.member.service;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MemberService {

//    Member login(MemberLoginRequestDto memberLoginRequestDto) throws Exception;
    ResponseEntity joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception;
    Member emailCheck(String email) throws Exception;
    Member nicknameCheck(String nickname) throws Exception;
//    /* Admin */
	List<MemberSearchResponseDto> getMemberList(String key) throws Exception;
    MemberInfoResponseDto getMemberInfo(String memberId) throws Exception;
    Member updateMemberPassword(MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception;
    ResponseEntity updateMemberInfo(MemberDetail memberDetail, MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) throws  Exception;

    void deleteMember(long memberId) throws Exception;
//    void saveRefreshToken(String memberid, String refreshToken) throws Exception;
//    Object getRefreshToken(String memberid) throws Exception;
//    void deleteRefreshToken(String memberid) throws Exception;
}

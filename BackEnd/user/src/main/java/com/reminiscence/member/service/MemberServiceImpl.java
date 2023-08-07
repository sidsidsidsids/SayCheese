package com.reminiscence.member.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.domain.Member;
import com.reminiscence.exception.customexception.MemberException;
import com.reminiscence.exception.message.MemberExceptionMessage;
import com.reminiscence.member.dto.*;
import com.reminiscence.member.repository.MemberRepository;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.MemberResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.HashMap;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;



    @Transactional
    @Override
    public void joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception {
        memberJoinRequestDto.setPassword(bCryptPasswordEncoder.encode(memberJoinRequestDto.getPassword()));
        Member member = memberJoinRequestDto.toEntity();
        // 이메일 중복 시 HttpStatus를 Already_Reported 상태로 응답 전달
        if (memberRepository.findByEmail(member.getEmail()) != null)
            throw new MemberException(MemberExceptionMessage.MEMBER_JOIN_FAILURE_EMAIL_DUPLICATED);
        // 닉네임 중복 시 HttpStatus를 Conflict 상태로 응답 전달
        if (memberRepository.findByNickname(member.getNickname()) != null)
            throw new MemberException(MemberExceptionMessage.MEMBER_JOIN_FAILURE_NICKNAME_DUPLICATED);
        memberRepository.save(member);
    }

    @Override
    public Member emailCheck(String email) throws Exception {
        return memberRepository.findByEmail(email);
    }

    @Override
    public Member nicknameCheck(String nickname) throws Exception {
        return memberRepository.findByNickname(nickname);
    }

    @Override
    @Transactional
    public ResponseEntity updateMemberInfo(MemberDetail memberDetail, MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) throws Exception {
        Member member = memberRepository.findById(memberDetail.getMember().getId()).orElse(null);

        member.modifyPassword(bCryptPasswordEncoder.encode(memberInfoUpdateRequestDto.getPassword()));
        member.modifyNickname(memberInfoUpdateRequestDto.getNickname());
        member.modifyGenderFm(memberInfoUpdateRequestDto.getGenderFm());
        member.modifyAge(memberInfoUpdateRequestDto.getAge());
        member.modifyName(memberInfoUpdateRequestDto.getName());
        member.modifyProfile(memberInfoUpdateRequestDto.getProfile());
        member.modifySnsId(memberInfoUpdateRequestDto.getSnsId());
        member.modifySnsType(memberInfoUpdateRequestDto.getSnsType());
        member.modifyPersonalAgreement(memberInfoUpdateRequestDto.getPersonalAgreement());
        Map<String, Object> response = new HashMap<>();

        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .genderFm(member.getGenderFm())
                .age(member.getAge())
                .name(member.getName())
                .profile(member.getProfile())
                .snsId(member.getSnsId())
                .snsType(member.getSnsType())
                .personalAgreement(member.getPersonalAgreement())
                .build();

        response.put("Member", memberInfoResponseDto);
        response.put("message", Response.of(MemberResponseMessage.MEMBER_UPDATE_SUCCESS));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public MemberInfoResponseDto getMemberInfo(String memberId) throws Exception {
        Member member = memberRepository.findByEmail(memberId);

        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .genderFm(member.getGenderFm())
                .age(member.getAge())
                .name(member.getName())
                .profile(member.getProfile())
                .snsId(member.getSnsId())
                .snsType(member.getSnsType())
                .personalAgreement(member.getPersonalAgreement())
                .build();

        return memberInfoResponseDto;
    }

    @Override
    public List<MemberSearchResponseDto> getMemberList(String key) throws SQLException {

        List<MemberSearchResponseDto> memberlist = memberRepository.searchMembers(key);
        return memberlist;
    }

    @Transactional
    @Override
    public Member updateMemberPassword(MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception {
        Member member = memberUpdatePasswordRequestDto.toEntity();
        return memberRepository.save(member);
    }

    @Transactional
    @Override
    public void deleteMember(long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).orElse(null);
        member.modifyDelYn('Y');
        memberRepository.save(member);
    }

//    @Override
//    public void saveRefreshToken(String memberId, String refreshToken) throws Exception {
////        Map<String, String> map = new HashMap<String, String>();
////        map.put("memberId", memberId);
////        map.put("token", refreshToken);
//        memberRepository.saveRefreshToken(memberId, refreshToken);
//    }
//
//    @Override
//    public Object getRefreshToken(String memberId) throws Exception {
//        return memberRepository.getRefreshToken(memberId);
//    }
//
//    @Override
//    public void deleteRefreshToken(String memberId) throws Exception {
////        Map<String, String> map = new HashMap<String, String>();
////        map.put("memberId", memberId);
////        map.put("token", null);
//        memberRepository.deleteRefreshToken(memberId, null);
//    }

}

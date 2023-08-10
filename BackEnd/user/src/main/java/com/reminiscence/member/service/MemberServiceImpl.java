package com.reminiscence.member.service;

import com.reminiscence.config.auth.MemberDetail;
import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import com.reminiscence.exception.customexception.MemberException;
import com.reminiscence.exception.message.MemberExceptionMessage;
import com.reminiscence.member.dto.*;
import com.reminiscence.member.repository.MemberRepository;
import com.reminiscence.message.Response;
import com.reminiscence.message.custom_message.MemberResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import java.util.HashMap;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
//import java.util.Map;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final String GUEST_PREFIX = "GUEST ";
    private final String GUEST_PASSWORD = "GUEST PASSWORD";


    @Transactional
    @Override
    public void joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception {
        memberJoinRequestDto.setPassword(bCryptPasswordEncoder.encode(memberJoinRequestDto.getPassword()));
        Member member = memberJoinRequestDto.toEntity();
        if(memberJoinRequestDto.getNickname().startsWith(GUEST_PREFIX))
            throw new MemberException(MemberExceptionMessage.MEMBER_JOIN_FAILURE_NICKNAME_PROTECTED);
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
        if(member != null)
//        member.modifyDelYn('Y');
//        memberRepository.save(member);

        memberRepository.delete(member);
    }

    @Transactional
    @Override
    public Member joinGuestMember(String nickname) throws SQLException {
        String email = UUID.randomUUID().toString();
        while(memberRepository.findByEmail(email) != null){
            email = UUID.randomUUID().toString();
        }
        String savedNickname = GUEST_PREFIX + nickname;
        while(memberRepository.findByNickname(savedNickname) != null){
            savedNickname = GUEST_PREFIX + nickname;
        }

        Member member = Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(GUEST_PASSWORD))
                .nickname(savedNickname)
                .role(Role.GUEST)
                .build();
        return memberRepository.save(member);
    }

    @Override
    public MemberNicknameResponseDto getMemberNickName(MemberDetail memberDetail){
        Member member = memberRepository.findById(memberDetail.getMember().getId()).orElse(null);
        String nickname;
        if(member == null){
            return null;
        } else if (memberDetail.getMember().getRole() == Role.GUEST) {
            nickname = member.getNickname().substring(GUEST_PREFIX.length()).trim();
            return new MemberNicknameResponseDto(nickname);
        } else {
            nickname = member.getNickname();
            return new MemberNicknameResponseDto(nickname);
        }
    }

}

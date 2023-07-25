package com.reminiscence.member.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reminiscence.domain.Member;
import com.reminiscence.member.dto.*;
import com.reminiscence.member.repository.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

//import java.util.HashMap;
import java.util.List;
//import java.util.Map;


@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberServiceImpl(MemberRepository memberRepository) {
        super();
        this.memberRepository = memberRepository;
    }

    @Override
    public Member login(MemberLoginRequestDto memberLoginRequestDto) throws Exception {

        Member member = memberLoginRequestDto.toEntity();
        String email = member.getEmail();
        String password = bCryptPasswordEncoder.encode(member.getPassword());
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MemberResponseDto memberResponseDto = objectMapper.convertValue(memberRepository.findByEmailAndPassword(email, password), MemberResponseDto.class);
        return memberResponseDto.toEntity();
    }

    @Override
    public ResponseEntity joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception {
        Member member = memberJoinRequestDto.toEntity();
        // 이메일 중복 시 HttpStatus를 Already_Reported 상태로 응답 전달
        if (memberRepository.findByEmail(member.getEmail()) != null)
            return new ResponseEntity<>(null, HttpStatus.ALREADY_REPORTED);
        // 닉네임 중복 시 HttpStatus를 Conflict 상태로 응답 전달
        if (memberRepository.findByNickname(member.getNickname()) != null)
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);

        return new ResponseEntity<>(memberRepository.save(member), HttpStatus.OK);
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
    public Member updateMemberInfo(MemberInfoUpdateRequestDto memberInfoUpdateRequestDto) throws Exception {
        Member member = memberInfoUpdateRequestDto.toEntity();
        return memberRepository.save(member);
    }

    @Override
    public Member getMemberInfo(String memberId) throws Exception {
        Member member = memberRepository.findByEmail(memberId);
        return member;
    }

    @Override
    public List<Member> getMemberList(String key) throws Exception {
        List<Member> memberlist = memberRepository.getMemberList(key);
        return memberlist;
    }

    @Override
    public Member updateMemberPassword(MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception {
        Member member = memberUpdatePasswordRequestDto.toEntity();
        return memberRepository.save(member);
    }

    @Override
    public void deleteMember(String memberId) throws Exception {
        memberRepository.deleteByEmail(memberId);
    }

    @Override
    public void saveRefreshToken(String memberId, String refreshToken) throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("memberId", memberId);
//        map.put("token", refreshToken);
        memberRepository.saveRefreshToken(memberId, refreshToken);
    }

    @Override
    public Object getRefreshToken(String memberId) throws Exception {
        return memberRepository.getRefreshToken(memberId);
    }

    @Override
    public void deleteRefreshToken(String memberId) throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("memberId", memberId);
//        map.put("token", null);
        memberRepository.deleteRefreshToken(memberId, null);
    }

}

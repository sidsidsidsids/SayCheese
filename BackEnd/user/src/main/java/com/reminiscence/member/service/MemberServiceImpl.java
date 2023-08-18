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
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;
    @Value("${cloud.aws.region.static}")
    private String BUCKET_REGION;


    @Transactional
    @Override
    public void joinMember(MemberJoinRequestDto memberJoinRequestDto) throws Exception {
        memberJoinRequestDto.setPassword(bCryptPasswordEncoder.encode(memberJoinRequestDto.getPassword()));
        Member member = memberJoinRequestDto.toEntity();
        if (memberJoinRequestDto.getNickname().startsWith(GUEST_PREFIX))
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
    public void updateMemberPassword(MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto) throws Exception {
        if (!memberUpdatePasswordRequestDto.getNewPassword().equals(memberUpdatePasswordRequestDto.getPasswordConfirm()))
            throw new MemberException(MemberExceptionMessage.MEMBER_PASSWORD_CONFIRM_FAILURE);
        Member member = memberRepository.findByEmail(memberUpdatePasswordRequestDto.getEmail());
        if (member == null) throw new MemberException(MemberExceptionMessage.DATA_NOT_FOUND);
        member.modifyPassword(bCryptPasswordEncoder.encode(memberUpdatePasswordRequestDto.getNewPassword()));
    }

    @Transactional
    @Override
    public void deleteMember(long memberId) throws Exception {
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member != null)
//        member.modifyDelYn('Y');
            memberRepository.delete(member);
    }

    @Transactional
    @Override
    public Member joinGuestMember(String nickname) throws SQLException {
        String email = UUID.randomUUID().toString();
        while (memberRepository.findByEmail(email) != null) {
            email = UUID.randomUUID().toString();
        }
        String savedNickname = GUEST_PREFIX + UUID.randomUUID() + " " + nickname;
        while (memberRepository.findByNickname(savedNickname) != null) {
            savedNickname = GUEST_PREFIX + UUID.randomUUID() + " " + nickname;
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
    public MemberNicknameResponseDto getMemberNickName(MemberDetail memberDetail) {
        Member member = memberDetail.getMember();
        String nickname;
        if (memberDetail.getMember().getRole() == Role.GUEST) {
            String[] nicknameParts = member.getNickname().split(" ");
            nickname = nicknameParts[nicknameParts.length-1];
        } else {
            nickname = member.getNickname();
        }
        return new MemberNicknameResponseDto(nickname);
    }

    @Transactional
    @Override
    public MemberProfileResponseDto saveProfile(MemberDetail memberDetail, MemberProfileSaveRequestDto requestDto) {
        Member member = memberRepository.findById(memberDetail.getMember().getId()).orElseThrow(() -> new MemberException(MemberExceptionMessage.DATA_NOT_FOUND));
        StringBuilder profileLink = new StringBuilder();
        profileLink.append("https://")
                .append(BUCKET_NAME)
                .append(".s3.")
                .append(BUCKET_REGION)
                .append(".amazonaws.com/")
                .append("profile")
                .append("/")
                .append(requestDto.getProfileName());
        member.modifyProfile(profileLink.toString());
        return new MemberProfileResponseDto(member.getProfile(), Response.of(MemberResponseMessage.MEMBER_PROFILE_MODIFY_SUCCESS));
    }
}

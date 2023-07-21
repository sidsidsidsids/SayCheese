package com.reminiscence.member.integration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import com.reminiscence.member.dto.MemberLoginRequestDto;
import com.reminiscence.member.dto.MemberResponseDto;
import com.reminiscence.member.repository.MemberRepository;
import com.reminiscence.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class MemberTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @After
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void testJoinMember() {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.Member)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getNickname()).isEqualTo(nickname);
        assertThat(member.getRole()).isEqualTo(Role.Member);
        assertThat(member.getGenderFm()).isEqualTo(genderFm);
        assertThat(member.getAge()).isEqualTo(age);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getProfile()).isEqualTo(profile);
        assertThat(member.getSnsId()).isEqualTo(snsId);
        assertThat(member.getSnsType()).isEqualTo(snsType);
    }

    @Test
    @DisplayName("로그인 성공")
    public void testLoginSuccess() throws SQLException {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.Member)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MemberResponseDto memberResponseDto = objectMapper.convertValue(memberRepository.findByEmailAndPassword(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword()), MemberResponseDto.class);

        assertThat(memberResponseDto).isNotNull();
    }


    @Test
    @DisplayName("로그인 실패")
    public void testLoginFail() throws SQLException {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";
        String nickname = "검정";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.Member)
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password("incorrect_password")
                .build();

        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MemberResponseDto memberResponseDto = objectMapper.convertValue(memberRepository.findByEmailAndPassword(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword()), MemberResponseDto.class);

        assertThat(memberResponseDto).isNull();
    }

//    @Test
//    public void 이메일검색_닉네임검색() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void 계정삭제() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void 회원명단불러오기() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void 토큰발급() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void 토큰삭제() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    public void 토큰재발급() {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        memberRepository.save(Member.builder()
//                .email(email)
//                .password(password)
//                .nickname("검정")
//                .role(Role.Member)
//                .genderFm('F')
//                .age(31)
//                .name("고무신")
//                .profile("xxxxxxxx")
//                .snsId("nosns")
//                .snsType("facebook")
//                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
//    }
}

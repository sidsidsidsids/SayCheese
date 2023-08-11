package com.reminiscence.member.unit;

import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import com.reminiscence.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Slf4j
public class MemberJpaTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버저장_블러오기")
    public void 멤버저장_블러오기() {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname("검정")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .snsId("nosns")
                .snsType("facebook")
                .build());
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(email);
//        assertThat(member.getPassword()).isEqualTo(password);
    }
//    @Test
//    @DisplayName("로그인")
//    public void 로그인() throws SQLException {
//        //given
//        String email = "b088081@gmail.com";
//        String password = "1234";
//
//        MemberResponseDto memberResponseDto = memberRepository.login(email, password);
//
//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getEmail()).isEqualTo(memberResponseDto.toEntity().getEmail());
//        assertThat(member.getPassword()).isEqualTo(memberResponseDto.toEntity().getEmail());
//    }
//    @Test
//    @DisplayName("이메일검색_닉네임검색")
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
//    @Test
//    @DisplayName("계정삭제")
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
//    @Test
//    @DisplayName("회원명단불러오기")
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
//    @Test
//    @DisplayName("토큰발급")
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
//    @Test
//    @DisplayName("토큰삭제")
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
//    @Test
//    @DisplayName("토큰재발급")
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

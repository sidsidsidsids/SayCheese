package com.reminiscence.user.repository;

import com.reminiscence.model.member.Member;
import com.reminiscence.repository.member.MemberRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @After
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void 멤버저장_블러오기() {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname("검정")
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .snsId("nosns")
                .snsType("facebook")
                .build());

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
    }
}

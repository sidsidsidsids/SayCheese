package com.reminiscence.config.auth;

import com.reminiscence.domain.Member;
import com.reminiscence.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService{

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("MemberDetailService : 진입");
        Member member = loadUserByEmail(email);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // session.setAttribute("loginUser", user);
        return new MemberDetail(member);
    }

    public Member loadUserByEmail(String email) throws UsernameNotFoundException {
        Member member = null;
        try {
            member = memberRepository.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return member;
    }
}

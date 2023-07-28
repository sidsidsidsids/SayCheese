package com.reminiscence.member.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.reminiscence.filter.JwtProperties;
import com.reminiscence.member.dto.MemberInfoUpdateRequestDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.reminiscence.JwtService;
import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
import com.reminiscence.member.dto.MemberJoinRequestDto;
import com.reminiscence.member.dto.MemberLoginRequestDto;
import com.reminiscence.member.dto.MemberResponseDto;
import com.reminiscence.member.repository.MemberRepository;
import com.reminiscence.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@Transactional
@Slf4j
public class MemberTest {

    @Autowired
    MemberService memberService;
    MockMvc mvc; // mockMvc 생성

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String adminToken;

    String memberToken;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private Environment env;
    @Autowired
    private WebApplicationContext applicationContext;

    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc= MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
//        Member admin=memberRepository.findById(1L).orElse(null);
//        Member member=memberRepository.findById(2L).orElse(null);
//        adminToken= JWT.create()
//                .withClaim("memberId",String.valueOf(admin.getId()))
//                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
//        memberToken= JWT.create()
//                .withClaim("memberId",String.valueOf(member.getId()))
//                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testJoinMemberSuccess() throws Exception {
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

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build();

        mvc.perform(post("/api/member/join")
                .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 응답 status를 ok로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(bCryptPasswordEncoder.matches(password, member.getPassword())).isTrue();
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
    @DisplayName("회원가입 실패 테스트_이메일 중복")
    public void testJoinMemberFailure_DuplicatedEmail() throws Exception {
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

        memberRepository.save(MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build().toEntity());

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email(email)
                .password("password")
                .nickname("nickname")
                .genderFm('F')
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build();

        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAlreadyReported()); // 응답 status를 alreadyReported로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        assertThat(membersList.size()).isOne();
    }

    @Test
    @DisplayName("회원가입 실패 테스트_닉네임 중복")
    public void testJoinMemberFailure_DuplicatedNickname() throws Exception {
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

        memberRepository.save(MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build().toEntity());

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email("email")
                .password("password")
                .nickname(nickname)
                .genderFm('F')
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build();

        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()); // 응답 status를 conflict로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        assertThat(membersList.size()).isOne();
    }

    @Test
    @DisplayName("로그인 성공")
    public void testLoginSuccess() throws Exception {
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
                .password(bCryptPasswordEncoder.encode(password))
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

//        assertThat(memberService.login(memberLoginRequestDto)).isNotNull();

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION));
//                .andExpect(jsonPath(email).value(email))
//                .andExpect(jsonPath(password).value(password));

//        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);;
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

//        MemberResponseDto memberResponseDto = objectMapper.convertValue(memberRepository.findByEmailAndPassword(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword()), MemberResponseDto.class);
//        assertThat(memberResponseDto).isNotNull();
    }


    @Test
    @DisplayName("로그인 실패")
    public void testLoginFail() throws Exception {
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

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION));

//        objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);;
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        MemberResponseDto memberResponseDto = objectMapper.convertValue(memberRepository.findByEmailAndPassword(memberLoginRequestDto.getEmail(), memberLoginRequestDto.getPassword()), MemberResponseDto.class);
//
//        assertThat(memberResponseDto).isNull();
    }

    @Test
    @DisplayName("회원정보 검색")
    public void testSearchMember() throws Exception {
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
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.Member)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        mvc.perform(get("/api/member/info/{memberId}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 응답 status를 ok로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(bCryptPasswordEncoder.matches(password, member.getPassword())).isTrue();
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
    @DisplayName("회원정보 수정")
    public void testUpdateMemberInfo() throws Exception {
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
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("nickname")
                .role(Role.Member)
                .genderFm('F')
                .age(30)
                .name("name")
                .profile("profile")
                .snsId("snsId")
                .snsType("snsType")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andReturn();

        MemberInfoUpdateRequestDto memberInfoUpdateRequestDto = MemberInfoUpdateRequestDto.builder()
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", result.getResponse().getHeader(JwtProperties.HEADER_STRING));
        mvc.perform(put("/api/member/modify")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(memberInfoUpdateRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 응답 status를 ok로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(bCryptPasswordEncoder.matches(password, member.getPassword())).isTrue();
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
    public void 계정삭제() throws Exception {
        //given
        String email = "b088081@gmail.com";
        String password = "1234";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname("검정")
                .role(Role.Member)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .snsId("nosns")
                .snsType("facebook")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", result.getResponse().getHeader(JwtProperties.HEADER_STRING));
        mvc.perform(delete("/api/member/delete")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // 응답 status를 ok로 테스트

        //when
        List<Member> membersList = memberRepository.findAll();

        //then
        Member member = membersList.get(0);
        assertThat(member.g()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
    }
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
//    public void 로그아웃() {
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

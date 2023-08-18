package com.reminiscence.member.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.reminiscence.config.redis.RedisKey;
import com.reminiscence.email.dummy.DummyEmailCheckRequestDto;
import com.reminiscence.email.dummy.DummyEmailRequestDto;
import com.reminiscence.filter.JwtProperties;
import com.reminiscence.filter.JwtTokenProvider;
import com.reminiscence.filter.JwtUtil;
import com.reminiscence.member.dto.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.reminiscence.JwtService;
import com.reminiscence.domain.Member;
import com.reminiscence.domain.Role;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static reactor.core.publisher.Mono.when;


@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@Transactional
@Slf4j
public class MemberIntegrationTest {
    @Autowired
    MemberService memberService;
    MockMvc mvc; // mockMvc 생성

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    RedisTemplate redisTemplate;
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
        // H2 Database에서 auto_increment 값을 초기화하는 쿼리 실행
//        jdbcTemplate.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    public void testJoinMemberSuccess() throws Exception {
        //givenz
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + "saycheese@gmail.com", "", Duration.ofMinutes(3));
        String email = "saycheese@gmail.com";
        String password = "password1234$";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .build();

        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디(이메일)").attributes(key("constraints").value("이메일 유효성 검사 제한")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value("비밀번호는 영문자, 특수문자, 숫자를 포함한 최소 8자 이상")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(key("constraints").value("닉네임은 최소 2자 이상, 최대 20자 이하, 공백이나 특수 기호 포함되지 않도록 제한, 중복 제한")),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별").attributes(key("constraints").value("")),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이").attributes(key("constraints").value("")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value("이름 입력 필수")),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 실패 테스트_이메일 중복")
    public void testJoinMemberFailure_DuplicatedEmail() throws Exception {
        //given
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + "saycheese@gmail.com", "", Duration.ofMinutes(3));
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";

        memberRepository.save(MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .build().toEntity());

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email(email)
                .password("password1234@")
                .nickname("nickname")
                .genderFm('F')
                .age(age)
                .name(name)
                .profile(profile)
                .build();

        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAlreadyReported())// 응답 status를 alreadyReported로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("중복된 아이디(이메일)").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value("비밀번호는 영문자, 특수문자, 숫자를 포함한 최소 8자 이상")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(key("constraints").value("닉네임은 최소 2자 이상, 최대 20자 이하, 공백이나 특수 기호 포함되지 않도록 제한, 중복 제한")),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별").attributes(key("constraints").value("")),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이").attributes(key("constraints").value("")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value("이름 입력 필수")),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 실패 테스트_닉네임 중복")
    public void testJoinMemberFailure_DuplicatedNickname() throws Exception {
        //given
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + "saycheese2@gmail.com", "", Duration.ofMinutes(3));
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";

        memberRepository.save(MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .build().toEntity());

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email("saycheese2@gmail.com")
                .password("password1234!")
                .nickname(nickname)
                .genderFm('F')
                .age(age)
                .name(name)
                .profile(profile)
                .build();
        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()) // 응답 status를 conflict로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디(이메일)").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value("비밀번호는 영문자, 특수문자, 숫자를 포함한 최소 8자 이상")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("중복된 닉네임").attributes(key("constraints").value("닉네임은 최소 2자 이상, 최대 20자 이하, 공백이나 특수 기호 포함되지 않도록 제한, 중복 제한")),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별").attributes(key("constraints").value("")),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이").attributes(key("constraints").value("")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value("이름 입력 필수")),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("회원가입 실패 테스트_비밀번호 제약")
    public void testJoinMemberFailure_PasswordConstraints() throws Exception {
        //given
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_SUCCESS_TOKEN_PREFIX + "saycheese2@gmail.com", "", Duration.ofMinutes(3));
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";

        memberRepository.save(MemberJoinRequestDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .build().toEntity());

        MemberJoinRequestDto memberJoinRequestDto = MemberJoinRequestDto.builder()
                .email("saycheese2@gmail.com")
                .password("1234!")
                .nickname(nickname)
                .genderFm('F')
                .age(age)
                .name(name)
                .profile(profile)
                .build();
        mvc.perform(post("/api/member/join")
                        .content(objectMapper.writeValueAsString(memberJoinRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 응답 status를 conflict로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디(이메일)").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value("비밀번호는 영문자, 특수문자, 숫자를 포함한 최소 8자 이상")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("중복된 닉네임").attributes(key("constraints").value("닉네임은 최소 2자 이상, 최대 20자 이하, 공백이나 특수 기호 포함되지 않도록 제한, 중복 제한")),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별").attributes(key("constraints").value("")),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이").attributes(key("constraints").value("")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value("이름 입력 필수")),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }


    @Test
    @DisplayName("아이디(이메일) 중복확인 성공 테스트")
    public void testIdCheckSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234asdf@";
        String nickname = "검정";
        Role role = Role.MEMBER;
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
                .genderFm(genderFm)
                .role(role)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        MemberIdCheckRequestDto memberIdCheckRequestDto = new MemberIdCheckRequestDto("anotherEmail");

        mvc.perform(post("/api/member/id-check")
                        .content(objectMapper.writeValueAsString(memberIdCheckRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestFields(
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("중복되지 않는 이메일").attributes(key("constraints").value(""))
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                                )
                        )
                );
    }

    @Test
    @DisplayName("아이디(이메일) 중복확인 실패 테스트")
    public void testIdCheckFailure() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
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
                .genderFm(genderFm)
                .role(role)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        MemberIdCheckRequestDto memberIdCheckRequestDto = new MemberIdCheckRequestDto(email);

        mvc.perform(post("/api/member/id-check")
                        .content(objectMapper.writeValueAsString(memberIdCheckRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 응답 status를 BadRequest로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("중복된 이메일").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 중복확인 성공 테스트")
    public void testNicknameCheckSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
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
                .genderFm(genderFm)
                .role(role)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        MemberNicknameCheckRequestDto memberNicknameCheckRequestDto = new MemberNicknameCheckRequestDto("anotherNickname");

        mvc.perform(post("/api/member/nickname-check")
                        .content(objectMapper.writeValueAsString(memberNicknameCheckRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("중복되지 않는 닉네임").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("닉네임 중복확인 실패 테스트")
    public void testNicknameCheckFailure() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
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
                .genderFm(genderFm)
                .role(role)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .build());

        MemberNicknameCheckRequestDto memberNicknameCheckRequestDto = new MemberNicknameCheckRequestDto(nickname);

        mvc.perform(post("/api/member/nickname-check")
                        .content(objectMapper.writeValueAsString(memberNicknameCheckRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 응답 status를 BadRequest로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("중복된 닉네임").attributes(key("constraints").value(""))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("로그인 성공")
    public void testLoginSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
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
                .role(Role.MEMBER)
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

        mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디(이메일)").attributes(key("constraints").value("")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value(""))
                        )
                ));
    }


    @Test
    @DisplayName("로그인 실패")
    public void testLoginFail() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";

        memberRepository.save(Member.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(Role.MEMBER)
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password("incorrect_password")
                .build();

        mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("아이디(이메일)").attributes(key("constraints").value("")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("틀린 비밀번호").attributes(key("constraints").value(""))
                        )
                ));
    }

    @Test
    @DisplayName("회원정보 조회")
    public void testGetMemberInfo() throws Exception {
        //given
        String email = "saycheese@gmail.com";
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
                .role(Role.MEMBER)
                .genderFm(genderFm)
                .age(age)
                .name(name)
                .profile(profile)
                .snsId(snsId)
                .snsType(snsType)
                .personalAgreement('T')
                .build());


        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();


        MvcResult result = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", result.getResponse().getHeader(JwtProperties.HEADER_STRING));

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        responseFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필"),
                                fieldWithPath("snsId").type(JsonFieldType.STRING).description("SNS ID"),
                                fieldWithPath("snsType").type(JsonFieldType.STRING).description("SNS 계정"),
                                fieldWithPath("personalAgreement").type(JsonFieldType.STRING).description("개인정보제공 동의 여부")
                        )
                ));
    }

    @Test
    @DisplayName("회원정보 수정")
    public void testUpdateMemberInfo() throws Exception {
        //given
        String email = "saycheese@gmail.com";
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
                .role(Role.MEMBER)
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

        MvcResult result = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
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
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호").attributes(key("constraints").value("비밀번호는 영문자, 특수문자, 숫자를 포함한 최소 8자 이상")),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임").attributes(key("constraints").value("닉네임은 최소 2자 이상, 최대 20자 이하, 공백이나 특수 기호 포함되지 않도록 제한, 중복 제한")),
                                fieldWithPath("genderFm").type(JsonFieldType.STRING).description("성별").attributes(key("constraints").value("")),
                                fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이").attributes(key("constraints").value("")),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value("이름 입력 필수")),
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필").attributes(key("constraints").value("")),
                                fieldWithPath("snsId").type(JsonFieldType.STRING).description("소셜 계정 아이디").attributes(key("constraints").value("")),
                                fieldWithPath("snsType").type(JsonFieldType.STRING).description("소셜 계정").attributes(key("constraints").value("")),
                                fieldWithPath("personalAgreement").type(JsonFieldType.STRING).description("개인정보제공 동의 여부").attributes(key("constraints").value("체크 필수"))
                        ),
                        responseFields(
                                fieldWithPath("message.message").type(JsonFieldType.STRING).description("API 응답 메시지"),
                                fieldWithPath("Member.email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("Member.nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("Member.genderFm").type(JsonFieldType.STRING).description("성별"),
                                fieldWithPath("Member.age").type(JsonFieldType.NUMBER).description("나이"),
                                fieldWithPath("Member.name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("Member.profile").type(JsonFieldType.STRING).description("프로필"),
                                fieldWithPath("Member.snsId").type(JsonFieldType.STRING).description("SNS ID"),
                                fieldWithPath("Member.snsType").type(JsonFieldType.STRING).description("SNS 계정"),
                                fieldWithPath("Member.personalAgreement").type(JsonFieldType.STRING).description("개인정보제공 동의 여부")
                        )
                ));
    }

    @Test
    @DisplayName("계정삭제 성공")
    public void testDeleteMemberSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("검정")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .delYn('N')
                .snsId("nosns")
                .snsType("facebook")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", result.getResponse().getHeader(JwtProperties.HEADER_STRING));
        mvc.perform(delete("/api/member/delete")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));

//        //when
//        List<Member> membersList = memberRepository.findAll();
//
//        //then
//        Member member = membersList.get(0);
//        assertThat(member.getDelYn()).isEqualTo('Y');
//        assertThat(member.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("계정삭제 실패")
    public void testDeleteMemberFailure() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("검정")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .delYn('N')
                .snsId("nosns")
                .snsType("facebook")
                .build());

        mvc.perform(delete("/api/member/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"
                ));
    }

    @Test
    @DisplayName("회원명단불러오기")
    public void testGetMembersList() throws Exception {
        //given
        String email = "memories";
        String nickname = "검정";

        memberRepository.save(Member.builder()
                .email("memories1@gmail.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("검정")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .delYn('N')
                .snsId("nosns")
                .snsType("facebook")
                .build());


        memberRepository.save(Member.builder()
                .email("memories2@gmail.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .delYn('N')
                .snsId("nosns")
                .snsType("facebook")
                .build());


        memberRepository.save(Member.builder()
                .email("memory1@gmail.com")
                .password(bCryptPasswordEncoder.encode("1234"))
                .nickname("검정색")
                .role(Role.MEMBER)
                .genderFm('F')
                .age(31)
                .name("고무신")
                .profile("xxxxxxxx")
                .delYn('N')
                .snsId("nosns")
                .snsType("facebook")
                .build());

        // 이메일로 검색 시
        MvcResult result1 = mvc.perform(get("/api/member/search-member")
                        .param("email-nickname", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("email-nickname").description("email 또는 nickname").attributes(key("constrains").value(""))
                        ),
                        responseFields(
                                fieldWithPath("[].email").type(JsonFieldType.STRING).description("아이디(이메일)"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임")
                        )
                ))
                .andReturn();

//        //when
//        String response1 = result1.getResponse().getContentAsString();
//        List<Member> membersList1 = objectMapper.readValue(response1, new TypeReference<List<Member>>() {
//        });
//
//        //then
//        assertThat(membersList1.size()).isEqualTo(2);
//        Member member_email1 = membersList1.get(0);
//        assertThat(member_email1.getEmail()).containsIgnoringCase(email);
//        Member member_email2 = membersList1.get(1);
//        assertThat(member_email2.getEmail()).containsIgnoringCase(email);
//
//        // 닉네임으로 검색 시
//        MvcResult result2 = mvc.perform(get("/api/member/search-member/{email-nickname}", nickname)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
//                .andReturn(); // 응답 status를 ok로 테스트
//
//        //when
//        String response2 = result2.getResponse().getContentAsString();
//        List<Member> membersList2 = objectMapper.readValue(response2, new TypeReference<List<Member>>() {
//        });
//
//        //then
//        assertThat(membersList2.size()).isEqualTo(2);
//        Member member_nickname1 = membersList2.get(0);
//        assertThat(member_nickname1.getNickname()).containsIgnoringCase(nickname);
//        Member member_nickname2 = membersList2.get(1);
//        assertThat(member_nickname2.getNickname()).containsIgnoringCase(nickname);
    }

    @Test
    @DisplayName("로그아웃 성공")
    public void testLogoutSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        headers.add(JwtProperties.HEADER_STRING, accessToken);

        mvc.perform(get("/api/logout")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("발급된 Access 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("AccessToken 만료 여부 판별(만료 시)")
    public void testAccessTokenExpiredConfirmed() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        String username = jwtTokenProvider.getUsernameFromToken(accessToken);

        Map<String, Object> claim = new HashMap<>();
        claim.put("memberId", memberRepository.findByEmail(username).getId());
        String newAccessToken = jwtTokenProvider.generateToken(username, 0, claim);
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newAccessToken);

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("만료된 Access 토큰 ")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("(AccessToken 만료 시) RefreshToken 검증 통해 AccessToken 재발급")
    public void testRefreshSuccess() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        String refreshToken = loginResult.getResponse().getHeader(JwtProperties.REFRESH_TOKEN_HEADER);

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> claim = new HashMap<>();
        String username = jwtTokenProvider.getUsernameFromToken(accessToken);
        claim.put("memberId", memberRepository.findByEmail(username).getId());
        String expiredAccessToken = jwtTokenProvider.generateToken(username, 0, claim);
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + expiredAccessToken);

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"));

        headers.set(JwtProperties.HEADER_STRING, refreshToken);
        System.out.println(refreshToken);

        mvc.perform(post("/api/auth/refresh")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("발급된(유효한) Refresh 토큰 ")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("AccessToken와 RefreshToken 모두 만료 시")
    public void testRefreshFail() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        String refreshToken = loginResult.getResponse().getHeader(JwtProperties.REFRESH_TOKEN_HEADER);

        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> claim = new HashMap<>();
        String username = jwtTokenProvider.getUsernameFromToken(accessToken);
        claim.put("memberId", memberRepository.findByEmail(username).getId());
        String expiredAccessToken = jwtTokenProvider.generateToken(username, 0, claim);
        headers.add(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + expiredAccessToken);

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"));

        String expiredRefreshToken = jwtTokenProvider.generateToken(username, 0, claim);
        headers.set(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + expiredRefreshToken);

        mvc.perform(post("/api/auth/refresh")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("유효기간 지난 Refresh 토큰 ")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("로그아웃 시 AccessToken 블랙리스트 설정 후 해당 AccessToken 접근 제한")
    public void testAccessTokenBlacklist() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        headers.add(JwtProperties.HEADER_STRING, accessToken);

        mvc.perform(get("/api/logout")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 Access 토큰")
                        )
                ));

        headers.set(JwtProperties.HEADER_STRING, accessToken);

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("이미 로그아웃 한 Access 토큰 ")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃 시 Redis 내 RefreshToken 삭제 후 RefreshToken 접근 제한")
    public void testRefreshTokenDeletion() throws Exception {
        //given
        String email = "saycheese@gmail.com";
        String password = "1234";
        String nickname = "검정";
        Role role = Role.MEMBER;
        char genderFm = 'F';
        int age = 31;
        String name = "고무신";
        String profile = "xxxxxxx";
        String snsId = "nosns";
        String snsType = "facebook";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname("빨강")
                .role(Role.MEMBER)
                .genderFm('M')
                .age(34)
                .name("나막신")
                .profile("yyyyyyyyyy")
                .snsId("snsno")
                .snsType("twitter")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult loginResult = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        String accessToken = loginResult.getResponse().getHeader(JwtProperties.HEADER_STRING);
        String refreshToken = loginResult.getResponse().getHeader(JwtProperties.REFRESH_TOKEN_HEADER);
        headers.add(JwtProperties.HEADER_STRING, accessToken);

        mvc.perform(get("/api/logout")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 Access 토큰")
                        )
                ));

        headers.set(JwtProperties.HEADER_STRING, refreshToken);

        mvc.perform(post("/api/auth/refresh")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized()) // 응답 status를 unauthorized로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("이미 로그아웃하여 삭제된 Refresh 토큰")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("게스트 토큰 발급")
    public void testGuestTokenIssue() throws Exception {
        String nickname = "thisIsNickname";

        mvc.perform(get("/api/auth/guest")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("nickname").attributes(key("constraints").value("빈 값도 가능(Optional)")).description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ))
                .andReturn();
    }

    @Test
    @DisplayName("게스트 토큰으로 회원 정보 조회 접근 시 실패")
    public void testGuestTokenForbiddenAccessFailure() throws Exception {
        String nickname = "thisIsNickname";

        MvcResult result = mvc.perform(get("/api/auth/guest")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("nickname").attributes(key("constraints").value("빈 값도 가능(Optional)")).description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, result.getResponse().getHeader(JwtProperties.HEADER_STRING));

        mvc.perform(get("/api/member/info")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()) // 응답 status를 forbidden으로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("게스트 토큰 ")
                        )
                ));
    }

    @Test
    @DisplayName("게스트 닉네임 조회")
    public void testGetGuestNickName() throws Exception {
        String nickname = "thisIsNickname";

        MvcResult result = mvc.perform(get("/api/auth/guest")
                        .param("nickname", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("nickname").attributes(key("constraints").value("빈 값도 가능(Optional)")).description("닉네임")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, result.getResponse().getHeader(JwtProperties.HEADER_STRING));

        mvc.perform(get("/api/member/nickname")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("게스트 AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("게스트 닉네임")
                        )
                ));
    }

    @Test
    @DisplayName("회원 닉네임 조회")
    public void testGetMemberNickName() throws Exception {
        String email = "kkk@naver.com";
        String password = "salmusa444";
        String nickname = "thisIsNickname";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
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

        MvcResult result = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, result.getResponse().getHeader(JwtProperties.HEADER_STRING));

        mvc.perform(get("/api/member/nickname")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("회원 AccessToken")
                        ),
                        responseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("회원 닉네임")
//                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("프로필 수정")
    public void testSaveMemberProfile() throws Exception {
        String email = "kkk@naver.com";
        String password = "salmusa444";
        String nickname = "thisIsNickname";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm('F')
                .age(30)
                .name("name")
                .build());

        MemberLoginRequestDto memberLoginRequestDto = MemberLoginRequestDto.builder()
                .email(email)
                .password(password)
                .build();

        MvcResult result = mvc.perform(post("/api/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"))
                .andReturn();

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtProperties.HEADER_STRING, result.getResponse().getHeader(JwtProperties.HEADER_STRING));

        String profileName = "thisIsProfile.jpg";

        MemberProfileSaveRequestDto memberProfileSaveRequestDto = new MemberProfileSaveRequestDto(profileName);

        mvc.perform(put("/api/member/profile")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(memberProfileSaveRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("회원 AccessToken")
                        ),
                        requestFields(
                                fieldWithPath("profileName").description("프로필 파일 이름").attributes(key("constraints").value("Not Null"))
                        ),
                        responseFields(
                                fieldWithPath("profile").type(JsonFieldType.STRING).description("프로필"),
                                fieldWithPath("response.message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));

    }

    @Test
    @DisplayName("비밀번호 변경 시 회원 이메일 찾기 및 이메일 전송(정상)")
    public void testFindMemberEmailAndTransportEmailSuccess() throws Exception {
        String email = "kkk@naver.com";
        String password = "salmusa444";
        String nickname = "thisIsNickname";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm('F')
                .age(30)
                .name("name")
                .profile("profile")
                .snsId("snsId")
                .snsType("snsType")
                .build());

        MemberFindPasswordRequestDto memberFindPasswordRequestDto = new MemberFindPasswordRequestDto(email);

        mvc.perform(post("/api/member/password")
                        .content(objectMapper.writeValueAsString(memberFindPasswordRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 응답 status를 ok로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").description("이메일").attributes(key("constraints").value("이메일 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경 시 회원 이메일 찾기(없는 이메일)")
    public void testFindMemberEmailFailure() throws Exception {
        String email = "kkk@naver.com";
        String password = "salmusa444";
        String nickname = "thisIsNickname";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm('F')
                .age(30)
                .name("name")
                .profile("profile")
                .snsId("snsId")
                .snsType("snsType")
                .build());

        MemberFindPasswordRequestDto memberFindPasswordRequestDto = new MemberFindPasswordRequestDto("notExistEmail");

        mvc.perform(post("/api/member/password")
                        .content(objectMapper.writeValueAsString(memberFindPasswordRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()) // 응답 status를 BadRequest로 테스트
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").description("이메일").attributes(key("constraints").value("이메일 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 확인 인증코드 검증 테스트")
    public void testCheckAuthTokenSuccess() throws Exception {
        String email = "wow@naver.com";
        String code = "1222";
        redisTemplate.opsForValue().set(RedisKey.EMAIL_AUTH_TOKEN_PREFIX + email, code, Duration.ofMinutes(3));

        DummyEmailCheckRequestDto dummyEmailCheckRequestDto = new DummyEmailCheckRequestDto(email, code);
        mvc.perform(post("/api/email/auth/check")
                        .content(objectMapper.writeValueAsString(dummyEmailCheckRequestDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건")),
                                fieldWithPath("token").type(JsonFieldType.STRING).description("인증 번호").attributes(key("constraints").value("빈 문자열이 아닌가?"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트(정상)")
    public void testModifyPasswordSuccess() throws Exception {
        String email = "wow@naver.com";
        String nickname = "thisIsNickname";
        String password = "salmusa444";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm('F')
                .age(30)
                .name("name")
                .profile("profile")
                .snsId("snsId")
                .snsType("snsType")
                .build());

        String newPassWord = "salmusa333";
        String passwordConfirm = "salmusa333";

        MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto = new MemberUpdatePasswordRequestDto(
                email, newPassWord, passwordConfirm);
        mvc.perform(put("/api/member/password")
                        .content(objectMapper.writeValueAsString(memberUpdatePasswordRequestDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건")),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호").attributes(key("constraints").value("비밀번호 제약조건")),
                                fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("비밀번호 확인").attributes(key("constraints").value("비밀번호 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트(없는 회원)")
    public void testWhenNotExistMember_ThenModifyPasswordFailure() throws Exception {
        String email = "wow@naver.com";
        String newPassWord = "salmusa444";
        String passwordConfirm = "salmusa444";

        MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto = new MemberUpdatePasswordRequestDto(
                email, newPassWord, passwordConfirm);

        mvc.perform(put("/api/member/password")
                        .content(objectMapper.writeValueAsString(memberUpdatePasswordRequestDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건")),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호").attributes(key("constraints").value("비밀번호 제약조건")),
                                fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("비밀번호 확인").attributes(key("constraints").value("비밀번호 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경 테스트(서로 다른 패스워드)")
    public void testWhenIncorrectPassword_ThenModifyPasswordFailure() throws Exception {
        String email = "wow@naver.com";
        String password = "salmusa444";
        String nickname = "thisIsNickname";

        memberRepository.save(Member.builder()
                .email(email)
                .password(bCryptPasswordEncoder.encode(password))
                .nickname(nickname)
                .role(Role.MEMBER)
                .genderFm('F')
                .age(30)
                .name("name")
                .profile("profile")
                .snsId("snsId")
                .snsType("snsType")
                .build());

        String newPassWord = "salmusa442";
        String passwordConfirm = "salmusa443";

        MemberUpdatePasswordRequestDto memberUpdatePasswordRequestDto = new MemberUpdatePasswordRequestDto(
                email, newPassWord, passwordConfirm);
        mvc.perform(put("/api/member/password")
                        .content(objectMapper.writeValueAsString(memberUpdatePasswordRequestDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건")),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새 비밀번호").attributes(key("constraints").value("비밀번호 제약조건")),
                                fieldWithPath("passwordConfirm").type(JsonFieldType.STRING).description("비밀번호 확인").attributes(key("constraints").value("비밀번호 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드")
                        )
                ));
    }
}

package com.reminiscence.article.notice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.config.SecurityConfig;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JWTAuthorizationFilter;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class NoticeIntegrationTest {

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    MockMvc mvc;
    @Autowired
    private Environment env;

    @Autowired
    SecurityFilterChain securityFilterChain;
    ObjectMapper objectMapper=new ObjectMapper();
    String adminToken;

    String memberToken;

    @BeforeEach
    public void init() throws SQLException {
        Member admin=memberRepository.findById(1L).orElse(null);
        Member member=memberRepository.findById(2L).orElse(null);
        adminToken= JWT.create()
                .withClaim("memberId",String.valueOf(admin.getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
        memberToken= JWT.create()
                .withClaim("memberId",String.valueOf(member.getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
    }

    @Test
    @DisplayName("공지 작성 테스트(정상)")
    public void writeNoticeArticleSuccessTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 10시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(post("/api/article/notice")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().isOk());

    }
    @Test
    @DisplayName("공지 작성 테스트(일반 사용자 시도)")
    public void writeNoticeArticleAuthFailTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 10시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(post("/api/article/notice")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().is4xxClientError());

    }

    @Test
    @DisplayName("공지 작성 테스트(짧은 제목으로 보낼 시)")
    public void writeNoticeArticleLowerSubjectFailTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("제목")
                .content("내일 10시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(post("/api/article/notice")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().isBadRequest());

    }




}

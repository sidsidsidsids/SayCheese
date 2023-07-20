package com.reminiscence.article.notice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.config.SecurityConfig;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JWTAuthorizationFilter;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
public class NoticeIntegrationTest {

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    MemberRepository memberRepository;
    MockMvc mvc;
    @Autowired
    private Environment env;

    ObjectMapper objectMapper=new ObjectMapper();
    String adminToken;

    String memberToken;

    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc=MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
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
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(status().isOk())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                                )
                        ));

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
                        .andExpect(status().isForbidden())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                                )
                        ));

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
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(status().isBadRequest())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                                ),
                                responseFields(
                                        fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                        ));;

    }



    @Test
    @DisplayName("공지 수정 테스트(정상)")
    public void modifyNoticeArticleSuccessTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 3시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(put("/api/article/notice/2")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("API 응답 메시지")
                        )
                ));

    }

    @Test
    @DisplayName("공지 수정 테스트(일반 사용자 시도)")
    public void modifyNoticeArticleAuthFailTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("시스템 점검 안내")
                .content("내일 3시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(put("/api/article/notice/2")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().isForbidden())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                                )
                        ));


    }

    @Test
    @DisplayName("공지 수정 테스트(짧은 제목으로 작성 시)")
    public void modifyNoticeArticleLowerSubjectFailTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("제목")
                .content("내일 3시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(put("/api/article/notice/2")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(status().isBadRequest())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                                ),
                                responseFields(
                                        fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                        ));


    }
    @Test
    @DisplayName("공지 수정 테스트(없는 번호로 요청 시)")
    public void modifyNoticeArticleNotExistsIdFailTest() throws Exception {
        DummyNoticeArticleRequestDto.Builder builder=new DummyNoticeArticleRequestDto.Builder();
        builder.subject("제목")
                .content("내일 3시에 점검 있어요");
        DummyNoticeArticleRequestDto dummyNoticeArticleRequestDto=builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(put("/api/article/notice/10000")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("제목").attributes(key("constraints").value("제목은 최소 3글자, 20글자 이하")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용").attributes(key("constraints").value("내용은 최소 3글자, 1000글자 이하"))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));


    }

    @Test
    @DisplayName("공지 조회 테스트")
    public void getNoticeArticleListSuccessTest() throws Exception {
        mvc.perform(get("/api/article/notice?page=3")
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestParameters(
                                parameterWithName("page").description("page")
                        ),
                        responseFields(
                                fieldWithPath("curPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                fieldWithPath("noticeArticleVoList").type(JsonFieldType.ARRAY).description("공지 글 목록 리스트"),
                                fieldWithPath("noticeArticleVoList[].id").type(JsonFieldType.NUMBER).description("글 번호"),
                                fieldWithPath("noticeArticleVoList[].subject").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("noticeArticleVoList[].writer").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("noticeArticleVoList[].hit").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("noticeArticleVoList[].createdDate").type(JsonFieldType.STRING).description("생성일")

                        )
                ));

    }



}

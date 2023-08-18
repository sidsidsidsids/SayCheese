package com.reminiscence.article.notice;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JwtUtil;
import com.reminiscence.article.member.repository.MemberRepository;
import com.reminiscence.article.notice.dummy.DummyNoticeArticleRequestDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    @Autowired
    private JwtUtil jwtUtil;

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

        Map<String, Object> adminClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(admin.getId()));
        Map<String, Object> memberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(member.getId()));

        final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분

        adminToken = jwtUtil.generateToken(admin.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, adminClaims);
        memberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, memberClaims);

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
        mvc.perform(RestDocumentationRequestBuilders.put("/api/article/notice/{noticeArticleId}",2)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("noticeArticleId").description("공지 글 번호")
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
        mvc.perform(RestDocumentationRequestBuilders.put("/api/article/notice/{noticeArticleId}",2)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().isForbidden())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                pathParameters(
                                        parameterWithName("noticeArticleId").description("공지 글 번호")
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
        mvc.perform(RestDocumentationRequestBuilders.put("/api/article/notice/{noticeArticleId}",2)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                        .andExpect(status().isBadRequest())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                pathParameters(
                                        parameterWithName("noticeArticleId").description("공지 글 번호")
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
        mvc.perform(RestDocumentationRequestBuilders.put("/api/article/notice/{noticeArticleId}",10000)
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyNoticeArticleRequestDto))
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("noticeArticleId").description("공지 글 번호")
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
    @DisplayName("공지 목록 조회 테스트(정상)")
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
                                fieldWithPath("pageNavigator").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                fieldWithPath("pageNavigator.curPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                fieldWithPath("pageNavigator.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                fieldWithPath("pageNavigator.totalDataCount").type(JsonFieldType.NUMBER).description("전체 데이터 개수"),
                                fieldWithPath("pageNavigator.prevNavigation").type(JsonFieldType.BOOLEAN).description("이전 페이지네이션 존재 유무"),
                                fieldWithPath("pageNavigator.nextNavigation").type(JsonFieldType.BOOLEAN).description("다음 페이지네이션 존재 유무"),
                                fieldWithPath("noticeArticleVoList").type(JsonFieldType.ARRAY).description("공지 글 목록 리스트"),
                                fieldWithPath("noticeArticleVoList[].id").type(JsonFieldType.NUMBER).description("글 번호"),
                                fieldWithPath("noticeArticleVoList[].subject").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("noticeArticleVoList[].writer").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("noticeArticleVoList[].hit").type(JsonFieldType.NUMBER).description("조회수"),
                                fieldWithPath("noticeArticleVoList[].createdDate").type(JsonFieldType.STRING).description("생성일")

                        )
                ));

    }

    @Test
    @DisplayName("공지 상세 조회 테스트(정상)")
    public void getNoticeArticleDetailSuccessTest() throws Exception {
        Long noticeArticleId=1L;
        mvc.perform(RestDocumentationRequestBuilders.get("/api/article/notice/{noticeArticleId}",noticeArticleId)
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("noticeArticleId").description("글 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("글번호"),
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("공지 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("공지 내용"),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자"),
                                fieldWithPath("hit").type(JsonFieldType.NUMBER).description("조회 수"),
                                fieldWithPath("createdDate").type(JsonFieldType.STRING).description("생성일")

                        )
                ));
    }
    @Test
    @DisplayName("공지 상세 조회 테스트(잘못된 번호가 들어올 시)")
    public void getNoticeArticleDetailNotExistsIdFailTest() throws Exception {
        Long noticeArticleId=1000000L;
        mvc.perform(RestDocumentationRequestBuilders.get("/api/article/notice/{noticeArticleId}",noticeArticleId)
                        .contentType("application/json"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        pathParameters(
                                parameterWithName("noticeArticleId").description("글 번호")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }



}

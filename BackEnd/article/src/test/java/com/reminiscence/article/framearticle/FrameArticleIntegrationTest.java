package com.reminiscence.article.framearticle;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.FrameSpecification;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.framearticle.dummy.DummyFrameArticleRequestDto;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
public class FrameArticleIntegrationTest {
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
        mvc= MockMvcBuilders.webAppContextSetup(applicationContext)
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
    @DisplayName("프레임 저장 테스트(정상)")
    public void writeFrameArticleTest() throws Exception{
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto=new DummyFrameArticleRequestDto.Builder()
                .name("test")
                .subject("test")
                .link("http://naver.com")
                .isPublic(true)
                .frameSpecification("A")
                .build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(post("/api/article/frame")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyFrameArticleRequestDto))
                        .contentType("application/json"))
                        .andExpect(status().isOk())
                        .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                                requestHeaders(
                                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                                ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("프레임 이름").attributes(key("constraints").value("최소 3글자, 100글자 이하")),
                                        fieldWithPath("link").type(JsonFieldType.STRING).description("프레임 링크 주소").attributes(key("constraints").value("최소 3글자, 최대 1000글자")),
                                        fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공개 여부").attributes(key("constraints").value("true or false")),
                                        fieldWithPath("frameSpecification").type(JsonFieldType.STRING).description("프레임 타입").attributes(key("constraints").value("A or B")),
                                        fieldWithPath("subject").type(JsonFieldType.STRING).description("프레임 게시글 제목").attributes(key("constraints").value("최소 3글자, 20글자 이하"))
                                ),
                                responseFields(
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                                )
                        ));
    }
    @Test
    @DisplayName("프레임 저장 테스트(비로그인 시도 시)")
    public void writeFrameArticleNotAuthFailTest() throws Exception{
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto=new DummyFrameArticleRequestDto.Builder()
                .name("test2")
                .subject("test2")
                .link("http://naver.com")
                .isPublic(true)
                .frameSpecification("A")
                .build();
        mvc.perform(post("/api/article/frame")
                        .content(objectMapper.writeValueAsString(dummyFrameArticleRequestDto))
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("프레임 이름").attributes(key("constraints").value("최소 3글자, 100글자 이하")),
                                fieldWithPath("link").type(JsonFieldType.STRING).description("프레임 링크 주소").attributes(key("constraints").value("최소 3글자, 최대 1000글자")),
                                fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공개 여부").attributes(key("constraints").value("true or false")),
                                fieldWithPath("frameSpecification").type(JsonFieldType.STRING).description("프레임 타입").attributes(key("constraints").value("A or B")),
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("프레임 게시글 제목").attributes(key("constraints").value("최소 3글자, 20글자 이하"))
                        )
                ));
    }
    @Test
    @DisplayName("프레임 저장 테스트(잘못된 값이 들어올 시)")
    public void writeFrameArticleValidFailTest() throws Exception{
        DummyFrameArticleRequestDto dummyFrameArticleRequestDto=new DummyFrameArticleRequestDto.Builder()
                .name("test2")
                .subject("test2")
                .link("http://naver.com")
                .isPublic(true)
                .frameSpecification("C")
                .build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(post("/api/article/frame")
                        .content(objectMapper.writeValueAsString(dummyFrameArticleRequestDto))
                        .headers(headers)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("프레임 이름").attributes(key("constraints").value("최소 3글자, 100글자 이하")),
                                fieldWithPath("link").type(JsonFieldType.STRING).description("프레임 링크 주소").attributes(key("constraints").value("최소 3글자, 최대 1000글자")),
                                fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN).description("공개 여부").attributes(key("constraints").value("true or false")),
                                fieldWithPath("frameSpecification").type(JsonFieldType.STRING).description("프레임 타입").attributes(key("constraints").value("A or B")),
                                fieldWithPath("subject").type(JsonFieldType.STRING).description("프레임 게시글 제목").attributes(key("constraints").value("최소 3글자, 20글자 이하"))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));
    }
}

package com.reminiscence.article.image;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.image.dummy.DummyImageArticleWriteRequestDto;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({RestDocumentationExtension.class,SpringExtension.class})
@SpringBootTest
public class ImageArticleTest {
    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    MockMvc mvc;
    @Autowired
    private Environment env;

    ObjectMapper objectMapper = new ObjectMapper();
    String adminToken;
    String memberToken;
    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc= MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
        Member admin = memberRepository.findById(1L).orElse(null);
        Member member = memberRepository.findById(2L).orElse(null);
        adminToken= JWT.create()
                .withClaim("memberId",String.valueOf(admin.getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
        memberToken= JWT.create()
                .withClaim("memberId",String.valueOf(member.getId()))
                .sign(Algorithm.HMAC512(env.getProperty("jwt.secret")));
    }

    @Test
    @DisplayName("이미지 게시글 상세조회 테스트(정상)")
    public void detailImageArticleSuccessTest() throws Exception{
        String articleId = "50";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(get("/api/article/image/{articleId}", articleId)
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("memberId").description("회원 ID"),
                                fieldWithPath("name").description("회원 닉네임"),
                                fieldWithPath("createdDate").description("게시글 작성일"),
                                fieldWithPath("loverCnt").description("좋아요 수"),
                                fieldWithPath("lover_yn").description("좋아요 여부(1 : O, 0 : X)"),
                                fieldWithPath("imgLink").description("이미지 링크"),
                                fieldWithPath("tags").description("게시글 주제(태그) 목록")
                        )));
    }

    @Test
    @DisplayName("이미지 게시글 작성 테스트(정상)")
    public void writeImageArticleSuccessTest() throws Exception {
        String imageId = "3";

        DummyImageArticleWriteRequestDto.Builder builder = new DummyImageArticleWriteRequestDto.Builder();
        builder.imageId(imageId);
        DummyImageArticleWriteRequestDto dummyImageArticleWriteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(post("/api/article/image")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleWriteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("imageId").type(JsonFieldType.STRING).description("이미지 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }




}

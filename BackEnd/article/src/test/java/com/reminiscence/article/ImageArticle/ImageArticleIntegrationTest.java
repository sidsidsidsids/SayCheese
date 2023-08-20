package com.reminiscence.article.ImageArticle;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.ImageArticle.dummy.DummyImageArticleDeleteRequestDto;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.ImageArticle.dummy.DummyImageArticleWriteRequestDto;
import com.reminiscence.article.filter.JwtUtil;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith({RestDocumentationExtension.class,SpringExtension.class})
@SpringBootTest
@Transactional
public class ImageArticleIntegrationTest {
    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtUtil jwtUtil;

    MockMvc mvc;
    @Autowired
    private Environment env;

    ObjectMapper objectMapper = new ObjectMapper();
    String adminToken;
    String memberToken;
    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) {
        mvc= MockMvcBuilders.webAppContextSetup(applicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity())
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
        Member admin = memberRepository.findById(1L).orElse(null);
        Member member = memberRepository.findById(2L).orElse(null);

        Map<String, Object> adminClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(admin.getId()));
        Map<String, Object> memberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(member.getId()));

        final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분

        adminToken = jwtUtil.generateToken(admin.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, adminClaims);
        memberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, memberClaims);

    }
    @Test
    @DisplayName("좋아요순 이미지 게시글 리스트 조회(정상)")
    public void listHotImageArticleSuccessTest() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + memberToken);
        mvc.perform(get("/api/article/image/list/hot")
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].imageLink").description("이미지 링크"),
                                fieldWithPath("[].loverCnt").description("좋아요 수"),
                                fieldWithPath("[].createdDate").description("게시글 작성일"),
                                fieldWithPath("[].author").description("게시글 작성자"),
                                fieldWithPath("[].loverYn").description("좋아요 여부")
                        )
                        ));
    }
    @Test
    @DisplayName("랜덤 이미지 게시글 리스트 조회(정상)")
    public void listRandomImageArticleSuccessTest() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + memberToken);
        mvc.perform(get("/api/article/image/list/random")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].imageLink").description("이미지 링크"),
                                fieldWithPath("[].loverCnt").description("좋아요 수"),
                                fieldWithPath("[].createdDate").description("게시글 작성일"),
                                fieldWithPath("[].author").description("게시글 작성자"),
                                fieldWithPath("[].loverYn").description("좋아요 여부")
                        )
                ));
    }
    @Test
    @DisplayName("랜덤 태그 기준 이미지 게시글 리스트 조회(정상)")
    public void listRandomTagImageArticleSuccessTest() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + memberToken);
        mvc.perform(get("/api/article/image/list/tag")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].imageLink").description("이미지 링크"),
                                fieldWithPath("[].loverCnt").description("좋아요 수"),
                                fieldWithPath("[].createdDate").description("게시글 작성일"),
                                fieldWithPath("[].author").description("게시글 작성자"),
                                fieldWithPath("[].loverYn").description("좋아요 여부")
                        )
                ));
    }
    @Test
    @DisplayName("태그 기준 이미지 게시글 리스트 조회(정상)")
    public void listTagImageArticleSuccessTest() throws Exception{
        String tagId = "1";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + memberToken);
        mvc.perform(get("/api/article/image/list/tag/{tagId}",tagId)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("tagId").description("태그 아이디")
                        ),
                        responseFields(
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].imageLink").description("이미지 링크"),
                                fieldWithPath("[].loverCnt").description("좋아요 수"),
                                fieldWithPath("[].createdDate").description("게시글 작성일"),
                                fieldWithPath("[].author").description("게시글 작성자"),
                                fieldWithPath("[].loverYn").description("좋아요 여부")
                        )
                ));
    }
    @Test
    @DisplayName("최신순 이미지 게시글 리스트 조회(정상)")
    public void listRecentImageArticleSuccessTest() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + memberToken);
        mvc.perform(get("/api/article/image/list/recent")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].articleId").description("게시글 ID"),
                                fieldWithPath("[].imageLink").description("이미지 링크"),
                                fieldWithPath("[].loverCnt").description("좋아요 수"),
                                fieldWithPath("[].createdDate").description("게시글 작성일"),
                                fieldWithPath("[].author").description("게시글 작성자"),
                                fieldWithPath("[].loverYn").description("좋아요 여부")
                        )
                ));
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
                                fieldWithPath("isMine").description("0: 손님, 1: 작성자"),
                                fieldWithPath("imageId").description("이미지 ID"),
                                fieldWithPath("author").description("회원 닉네임"),
                                fieldWithPath("createdDate").description("게시글 작성일"),
                                fieldWithPath("imgLink").description("이미지 링크"),
                                fieldWithPath("loverCnt").description("좋아요 수"),
                                fieldWithPath("loverYn").description("좋아요 여부(1 : O, 0 : X)"),
                                fieldWithPath("tags").description("게시글 주제(태그) 목록")
                        )));
    }
    @Test
    @DisplayName("존재하지 않는 이미지 게시글 상세조회 테스트(비정상)")
    public void detailNonImageArticleFailTest() throws Exception{
        String articleId = "100";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(get("/api/article/image/{articleId}", articleId)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID")
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

    @Test
    @DisplayName("이미지가 없는 게시글 작성 테스트(비정상)")
    public void writeImageArticleFailTest() throws Exception {
        String imageId = "50";

        DummyImageArticleWriteRequestDto.Builder builder = new DummyImageArticleWriteRequestDto.Builder();
        builder.imageId(imageId);
        DummyImageArticleWriteRequestDto dummyImageArticleWriteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(post("/api/article/image")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleWriteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("imageId").type(JsonFieldType.STRING).description("이미지 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("이미지 게시글만 삭제 테스트(정상)")
    public void deleteOnlyImageArticleSuccessTest() throws Exception {
        String articleId = "50";

        DummyImageArticleDeleteRequestDto.Builder builder = new DummyImageArticleDeleteRequestDto.Builder();
        builder.imageId(articleId);
        DummyImageArticleDeleteRequestDto dummyImageArticleDeleteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + adminToken);
        mvc.perform(delete("/api/article/image")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleDeleteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("articleId").type(JsonFieldType.STRING).description("게시글 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("잘못된 사용자 이미지 게시글만 삭제 접근 테스트(비정상)")
    public void deleteWrongUserOnlyImageArticleFailTest() throws Exception {
        String articleId = "50";
        DummyImageArticleDeleteRequestDto.Builder builder = new DummyImageArticleDeleteRequestDto.Builder();
        builder.imageId(articleId);
        DummyImageArticleDeleteRequestDto dummyImageArticleDeleteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(delete("/api/article/image")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleDeleteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("articleId").type(JsonFieldType.STRING).description("게시글 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("이미지 게시글 삭제 테스트(정상)")
    public void deleteImageArticleSuccessTest() throws Exception {
        String articleId = "50";
        DummyImageArticleDeleteRequestDto.Builder builder = new DummyImageArticleDeleteRequestDto.Builder();
        builder.imageId(articleId);
        DummyImageArticleDeleteRequestDto dummyImageArticleDeleteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + adminToken);
        mvc.perform(delete("/api/article/image/all")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleDeleteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("articleId").type(JsonFieldType.STRING).description("게시글 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("잘못된 사용자 이미지 게시글 삭제 테스트(비정상)")
    public void deleteWrongUserImageArticleSuccessTest() throws Exception {
        String articleId = "50";
        DummyImageArticleDeleteRequestDto.Builder builder = new DummyImageArticleDeleteRequestDto.Builder();
        builder.imageId(articleId);
        DummyImageArticleDeleteRequestDto dummyImageArticleDeleteRequestDto = builder.build();
        HttpHeaders headers=new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);
        mvc.perform(delete("/api/article/image/all")
                        .headers(headers)
                        .content(objectMapper.writeValueAsString(dummyImageArticleDeleteRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("articleId").type(JsonFieldType.STRING).description("게시글 ID").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
}

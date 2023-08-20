package com.reminiscence.article.Lover;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JwtUtil;
import com.reminiscence.article.lover.repository.LoverRepository;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
public class LoverIntegrationTest {

    @Autowired
    LoverRepository loverRepository;

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
        Map<String, Object> adminClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(admin.getId()));
        Map<String, Object> memberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(member.getId()));

        final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분

        adminToken = jwtUtil.generateToken(admin.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, adminClaims);
        memberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, memberClaims);

    }

    @Test
    @DisplayName("좋아요 추가 테스트(정상)")
    public void insertLoverSuccessTest() throws Exception {
        Long articleId = 58L;
        String articleType = "image";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);

        mvc.perform(post("/api/article/lover/{articleType}/{articleId}",articleType, articleId)
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID"),
                                parameterWithName("articleType").description("게시글 타입 : image, frame")
                        )
                ));
    }

    @Test
    @DisplayName("게시글 존재하지 않을 때 좋아요 추가 테스트(비정상)")
    public void insertLoverFailTest() throws Exception {
        Long articleId = 100L;
        String articleType = "image";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(post("/api/article/lover/{articleType}/{articleId}", articleType, articleId)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID"),
                                parameterWithName("articleType").description("게시글 타입 : image, frame")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").description("HTTP 상태코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }

    @Test
    @DisplayName("좋아요 삭제 테스트(정상)")
    public void deleteLoverSuccessTest() throws Exception {
        Long articleId = 50L;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + memberToken);

        mvc.perform(delete("/api/article/lover/{articleId}", articleId)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID")
                        )
                ));
    }
    @Test
    @DisplayName("존재하지 않는 좋아요 삭제 테스트(비정상)")
    public void deleteLoverFailTest() throws Exception {
        Long articleId = 100L;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+memberToken);
        mvc.perform(delete("/api/article/lover/{articleId}", articleId)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰")
                        ),
                        pathParameters(
                                parameterWithName("articleId").description("이미지 게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").description("HTTP 상태코드"),
                                fieldWithPath("message").description("에러 메시지")
                        )
                ));
    }
}

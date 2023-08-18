package com.reminiscence.article.image;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JwtUtil;
import com.reminiscence.article.image.dummy.DummyDeleteImageOwnerRequestDto;
import com.reminiscence.article.member.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@Transactional
public class ImageIntegrationTest {
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
    @DisplayName("소유한 이미지 최근 기준 조회(정상)")
    public void getNoticeArticleListSuccessTest() throws Exception {
        int page = 1;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ adminToken);
        mvc.perform(get("/api/image?page=" + page)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("jwt 토큰")),
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
                                fieldWithPath("imageVoList").type(JsonFieldType.ARRAY).description("공지 글 목록 리스트"),
                                fieldWithPath("imageVoList[].imageId").type(JsonFieldType.NUMBER).description("글 번호"),
                                fieldWithPath("imageVoList[].imageLink").type(JsonFieldType.STRING).description("이미지 링크"),
                                fieldWithPath("imageVoList[].createdDate").type(JsonFieldType.STRING).description("이미지 저장일"),
                                fieldWithPath("imageVoList[].loverCnt").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                                fieldWithPath("imageVoList[].articleYn").type(JsonFieldType.STRING).description("게시글 존재 여부")
                                )
                ));
    }

    @Test
    @DisplayName("이미지 소유자 삭제 테스트")
    public void deleteImageOwnerSuccessTest() throws Exception {
        Long imageId = 2L;
        DummyDeleteImageOwnerRequestDto.Builder builder = new DummyDeleteImageOwnerRequestDto.Builder();
        builder.imageId(imageId);
        DummyDeleteImageOwnerRequestDto dummyRequestDto = builder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(delete("/api/image")
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dummyRequestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("imageId").type(JsonFieldType.NUMBER).description("이미지 ID").attributes(key("constraints").value("Not Null"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("이미지 소유자 삭제 성공 메시지")
                        )
                ));
    }
    @Test
    @DisplayName("랜덤 태그 조회 테스트")
    public void readRandomTagSuccessTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(get("/api/image/random/tag")
                        .headers(headers))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        responseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("태그 ID"),
                                fieldWithPath("[].tag").type(JsonFieldType.STRING).description("태그 이름")
                        )
                ));
    }
}

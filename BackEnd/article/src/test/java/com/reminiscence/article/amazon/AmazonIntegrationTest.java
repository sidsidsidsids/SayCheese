package com.reminiscence.article.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.article.amazon.dto.DummyPreSignRequestDto;
import com.reminiscence.article.amazon.dto.FileType;
import com.reminiscence.article.config.TestS3Config;
import com.reminiscence.article.domain.Member;
import com.reminiscence.article.filter.JwtUtil;
import com.reminiscence.article.member.repository.MemberRepository;
import io.findify.s3mock.S3Mock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@Import({TestS3Config.class})
@ActiveProfiles("test")
public class AmazonIntegrationTest {
    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    private static final String BUCKET_NAME = "test-bucket";
    MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Environment env;

    ObjectMapper objectMapper = new ObjectMapper();
    String adminToken;
    String memberToken;

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }
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
    @DisplayName("이미지 PreSignedUrl 생성 테스트")
    public void createPreSignedUrlTest() throws Exception{
        String fileName = "test.jpg";
        FileType fileType = FileType.IMAGE;
        DummyPreSignRequestDto.Builder builder = new DummyPreSignRequestDto.Builder();
        builder.fileName(fileName);
        builder.fileType(fileType);
        DummyPreSignRequestDto dummyRequestDto = builder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(
                        post("/api/amazon/presigned")
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dummyRequestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일 이름").attributes(key("constraints").value("Not Null")),
                                fieldWithPath("fileType").type(JsonFieldType.STRING).description("파일 타입").attributes(key("constraints").value("[Image, Frame, Profile]"))
                        ),
                        responseFields(
                                fieldWithPath("preSignUrl").type(JsonFieldType.STRING).description("파일 업로드 URL"),
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("UUID로 저장될 파일 이름")
                        )
                ));
    }
    @Test
    @DisplayName("프로필 PreSignedUrl 생성 테스트")
    public void createProfilePreSignedUrlTest() throws Exception{
        String fileName = "test.jpg";
        FileType fileType = FileType.PROFILE;
        DummyPreSignRequestDto.Builder builder = new DummyPreSignRequestDto.Builder();
        builder.fileName(fileName);
        builder.fileType(fileType);
        DummyPreSignRequestDto dummyRequestDto = builder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(
                        post("/api/amazon/presigned")
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dummyRequestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일 이름").attributes(key("constraints").value("Not Null")),
                                fieldWithPath("fileType").type(JsonFieldType.STRING).description("파일 타입").attributes(key("constraints").value("[Image, Frame, Profile]"))
                        ),
                        responseFields(
                                fieldWithPath("preSignUrl").type(JsonFieldType.STRING).description("파일 업로드 URL"),
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("UUID로 저장될 파일 이름")
                        )
                ));
    }
    @Test
    @DisplayName("프레임 PreSignedUrl 생성 테스트")
    public void createFramePreSignedUrlTest() throws Exception{
        String fileName = "test.jpg";
        FileType fileType = FileType.FRAME;
        DummyPreSignRequestDto.Builder builder = new DummyPreSignRequestDto.Builder();
        builder.fileName(fileName);
        builder.fileType(fileType);
        DummyPreSignRequestDto dummyRequestDto = builder.build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+adminToken);
        mvc.perform(
                        post("/api/amazon/presigned")
                                .headers(headers)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dummyRequestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("파일 이름").attributes(key("constraints").value("Not Null")),
                                fieldWithPath("fileType").type(JsonFieldType.STRING).description("파일 타입").attributes(key("constraints").value("[Image, Frame, Profile]"))
                        ),
                        responseFields(
                                fieldWithPath("preSignUrl").type(JsonFieldType.STRING).description("파일 업로드 URL"),
                                fieldWithPath("fileName").type(JsonFieldType.STRING).description("UUID로 저장될 파일 이름")
                        )
                ));
    }

}

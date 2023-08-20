package com.reminiscence.room.participant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.room.domain.Member;
import com.reminiscence.room.filter.JwtUtil;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.participant.dto.DummyDeleteParticipantRequestDto;
import com.reminiscence.room.participant.dto.DummyUpdateConnectionYnParticipantRequestDto;
import com.reminiscence.room.participant.dto.DummyUpdateStreamIdParticipantRequestDto;
import com.reminiscence.room.participant.dto.DummyWriteParticipantRequestDto;
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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ParticipantIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private JwtUtil jwtUtil;

    MockMvc mvc;
    @Autowired
    private Environment env;

    ObjectMapper objectMapper = new ObjectMapper();
    String guestToken;
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
        Member guest = memberRepository.findById(5L).orElse(null);
        Member member = memberRepository.findById(3L).orElse(null);

        Map<String, Object> guestClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(guest.getId()));
        Map<String, Object> memberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(member.getId()));

        final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분

        guestToken = jwtUtil.generateToken(guest.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, guestClaims);
        memberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, memberClaims);

    }

    @Test
    @DisplayName("참가자 생성 테스트(정상)")
    public void writeParticipantSuccessTest() throws Exception{
        DummyWriteParticipantRequestDto requestDto = DummyWriteParticipantRequestDto.builder()
                .roomCode("sessionA")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/participant")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("존재하지 않는 방 참가자 생성 테스트(비정상)")
    public void writeInvalidRoomParticipantFailTest() throws Exception{
        DummyWriteParticipantRequestDto requestDto =
                DummyWriteParticipantRequestDto.builder().roomCode("testRoom")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/participant")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("방장 변경 테스트(정상)")
    public void updateOwnerParticipantSuccessTest() throws Exception {
        String roomCode = "sessionA";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(put("/api/participant/{roomCode}/owner", roomCode)
                .headers(headers))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"
                ,requestHeaders(
                        headerWithName("Authorization").description("로그인 성공한 토큰 ")
                )
                ,pathParameters(
                        parameterWithName("roomCode").description("방 코드")
                )));
    }
    @Test
    @DisplayName("참여자 스트림 ID 변경 테스트(정상)")
    public void updateStreamIdParticipantSuccessTest() throws Exception {
        String roomCode = "sessionA";
        DummyUpdateStreamIdParticipantRequestDto requestDto = DummyUpdateStreamIdParticipantRequestDto.builder()
                .streamId("test")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(put("/api/participant/{roomCode}/streamId", roomCode)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"
                        ,requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                        ,pathParameters(
                                parameterWithName("roomCode").description("방 코드")
                        )
                        ,requestFields(
                                fieldWithPath("streamId").description("스트림 ID").attributes(key("constraints").value("Not Null"))
                        )));
    }
    @Test
    @DisplayName("참여자 ConnectionN 변경 테스트(정상)")
    public void updateConnectionYnParticipantSuccessTest() throws Exception {
        String roomCode = "sessionA";
        DummyUpdateConnectionYnParticipantRequestDto requestDto = DummyUpdateConnectionYnParticipantRequestDto.builder()
                .streamId("asdfasdfa")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(put("/api/participant/fail/connection/{roomCode}", roomCode)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"
                        ,requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                        ,pathParameters(
                                parameterWithName("roomCode").description("방 코드")
                        )
                        ,requestFields(
                                fieldWithPath("streamId").description("스트림 아이디").attributes(key("constraints").value("Not Null"))
                        )));
    }
    @Test
    @DisplayName("참가자 삭제 테스트(정상)")
    public void deleteParticipantSuccessTest() throws Exception{
        DummyDeleteParticipantRequestDto.Builder builder = new DummyDeleteParticipantRequestDto.Builder();
        DummyDeleteParticipantRequestDto requestDto =
                builder.roomCode("sessionA")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(delete("/api/participant")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }


    @Test
    @DisplayName("방이 존재하지 않는 참여자 삭제 테스트(비정상)")
    public void deleteInvalidRoomParticipantFailTest() throws Exception{
        DummyDeleteParticipantRequestDto.Builder builder = new DummyDeleteParticipantRequestDto.Builder();
        DummyDeleteParticipantRequestDto requestDto =
                builder.roomCode("testRoom")
                        .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(delete("/api/participant")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }
    @Test
    @DisplayName("존재하지 않는 참가자 삭제 테스트(비정상)")
    public void deleteInvalidParticipantFailTest() throws Exception{
        DummyDeleteParticipantRequestDto.Builder builder = new DummyDeleteParticipantRequestDto.Builder();
        DummyDeleteParticipantRequestDto requestDto =
                builder.roomCode("sessionB")
                        .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(delete("/api/participant")
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null"))
                        )
                ));
    }

}

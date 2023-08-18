package com.reminiscence.room.room;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.room.domain.Member;
import com.reminiscence.room.domain.Mode;
import com.reminiscence.room.domain.Specification;
import com.reminiscence.room.filter.JwtUtil;
import com.reminiscence.room.member.repository.MemberRepository;
import com.reminiscence.room.room.dto.DummyRoomCheckRequestDto;
import com.reminiscence.room.room.dto.DummyRoomCreateRequestDto;
import com.reminiscence.room.room.service.RoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RoomIntegrationTest {
    @Autowired
    WebApplicationContext applicationContext;

    @Autowired
    private MemberRepository memberRepository;

    @SpyBean
    private RoomServiceImpl roomService;

    @Autowired
    private JwtUtil jwtUtil;

    MockMvc mvc;
    @Autowired
    private Environment env;

    ObjectMapper objectMapper = new ObjectMapper();
    String guestToken;
    String connectionGuestToken;
    String memberToken;
    String notConnectionMemberToken;


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
        Member connectionGuest = memberRepository.findById(7L).orElse(null);
        Member member = memberRepository.findById(2L).orElse(null);
        Member notConnectionMember = memberRepository.findById(6L).orElse(null);

        Map<String, Object> guestClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(guest.getId()));
        Map<String, Object> connectionGuestClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(connectionGuest.getId()));
        Map<String, Object> memberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(member.getId()));
        Map<String, Object> notConnectionMemberClaims = jwtUtil.setCustomClaims(new HashMap<>(), "memberId", String.valueOf(notConnectionMember.getId()));

        final int ACCESS_TOKEN_EXPIRATION_TIME = 60 * 30 * 1000 ; // 30분

        guestToken = jwtUtil.generateToken(guest.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, guestClaims);
        connectionGuestToken = jwtUtil.generateToken(guest.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, connectionGuestClaims);
        memberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, memberClaims);
        notConnectionMemberToken = jwtUtil.generateToken(member.getEmail(), ACCESS_TOKEN_EXPIRATION_TIME, notConnectionMemberClaims);
    }


    @Test
    @DisplayName("방 정보 조회 테스트(정상)")
    public void readRoomInfoSuccessTest() throws Exception {

        String roomCode = "sessionA";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(get("/api/room")
                .headers(headers)
                .param("roomCode",roomCode))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestParameters(
                                parameterWithName("roomCode").description("방 코드")
                        ),
                        responseFields(
                                fieldWithPath("specification").description("프레임 규격"),
                                fieldWithPath("mode").description("방 모드")
                        )
                ));
    }

    @Test
    @DisplayName("방 연결 확인 테스트(정상, 회원)")
    public void checkRoomConnectionMemberSuccessTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ notConnectionMemberToken);
        mvc.perform(post("/api/room/check")
                .headers(headers))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                ));
    }
    @Test
    @DisplayName("방 연결 확인 테스트(비정상, 회원)")
    public void checkRoomConnectionMemberFailTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(post("/api/room/check")
                        .headers(headers))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                ));
    }

    @Test
    @DisplayName("방 연결 확인 테스트(정상, AccessToken 있는 비회원)")
    public void checkRoomConnectionGuestSuccessTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/room/check")
                        .headers(headers))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                ));
    }
    @Test
    @DisplayName("방 연결 확인 테스트(정상, AccessToken 없는 비회원)")
    public void checkRoomConnectionNonAccessGuestSuccessTest() throws Exception {
        mvc.perform(post("/api/room/check"))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}"
                ));
    }
    @Test
    @DisplayName("방 연결 확인 테스트(비정상, AccessToken 있는 비회원)")
    public void checkRoomConnectionGuestFailTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ connectionGuestToken);
        mvc.perform(post("/api/room/check")
                        .headers(headers))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        )
                ));
    }

    @Test
    @DisplayName("방 비밀번호 확인 테스트(정상)")
    public void checkRoomPasswordSuccessTest() throws Exception{
        String roomCode = "sessionB";
        String password = "1235";
        when(roomService.checkSession(any())).thenReturn(true);
        DummyRoomCheckRequestDto requestDto = DummyRoomCheckRequestDto
                .builder()
                .password(password)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/room/check/{roomCode}", roomCode)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("roomCode").description("방 코드")
                        ),
                        requestFields(
                                fieldWithPath("password").description("방 비밀번호").attributes(key("constraints").value("Not Null"))
                        )
                ));

    }
    @Test
    @DisplayName("방 비밀번호 틀렸을 때 테스트(비정상)")
    public void checkRoomNotEqPasswordFailTest() throws Exception{
        String roomCode = "sessionA";
        DummyRoomCheckRequestDto requestDto = DummyRoomCheckRequestDto
                .builder()
                .password("21234123")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/room/check/{roomCode}", roomCode)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("roomCode").description("방 코드")
                        ),
                        requestFields(
                                fieldWithPath("password").description("방 비밀번호").attributes(key("constraints").value("Not Null"))
                        )
                ));

    }
    @Test
    @DisplayName("잘못된 방 코드 접근 테스트(비정상)")
    public void checkRoomNotCodePasswordFailTest() throws Exception{
        String roomCode = "tussle";
        DummyRoomCheckRequestDto requestDto = DummyRoomCheckRequestDto
                .builder()
                .password("1234")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ guestToken);
        mvc.perform(post("/api/room/check/{roomCode}", roomCode)
                        .headers(headers)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("roomCode").description("방 코드")
                        ),
                        requestFields(
                                fieldWithPath("password").description("방 비밀번호").attributes(key("constraints").value("Not Null"))
                        )
                ));

    }

    @Test
    @DisplayName("방 생성 테스트(정상)")
    public void createRoomSuccessTest() throws Exception {
        DummyRoomCreateRequestDto requestDto =
                DummyRoomCreateRequestDto.builder().password("1234")
                .roomCode("tussle")
                .mode(Mode.GAME)
                .maxCount(4)
                .specification(Specification.HORIZONTAL)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+guestToken);
        mvc.perform(post("/api/room")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        requestFields(
                                fieldWithPath("roomCode").description("방 코드").attributes(key("constraints").value("Not Null")),
                                fieldWithPath("password").description("방 비밀번호").attributes(key("constraints").value("Not Null")),
                                fieldWithPath("mode").description("방 모드").attributes(key("constraints").value(" game, normal 중 하나 선택")),
                                fieldWithPath("maxCount").description("방 최대 인원").attributes(key("constraints").value("최소 1명, 최대 4명")),
                                fieldWithPath("specification").description("방 규격").attributes(key("constraints").value("gradle, row 중 하나 선택"))
                        )
                )
        );
    }
    @Test
    @DisplayName("방 시작여부 변경 테스트(정상)")
    public void updateRoomStartYnSuccessTest() throws Exception {
        String roomCode = "sessionA";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ memberToken);
        mvc.perform(put("/api/room/{roomCode}/start", roomCode)
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
    @DisplayName("방 삭제 테스트(정상)")
    public void deleteRoomSuccessTest() throws Exception{
        String roomCode = "sessionA";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+guestToken);
        mvc.perform(delete("/api/room/{roomCode}", roomCode)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestHeaders(
                                headerWithName("Authorization").description("로그인 성공한 토큰 ")
                        ),
                        pathParameters(
                                parameterWithName("roomCode").description("방 코드"))
                        ));
    }
}

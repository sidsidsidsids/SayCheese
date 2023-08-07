package com.reminiscence.email;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reminiscence.email.dto.EmailRequestDto;
import com.reminiscence.email.dummy.DummyEmailRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.mail.internet.MimeMessage;
import java.sql.SQLException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@Transactional
public class EmailIntegrationTest {

    MockMvc mvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    private Environment env;
    @Autowired
    private WebApplicationContext applicationContext;
    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();

    }

    @Test
    @DisplayName("이메일 전송 테스트(정상)")
    public void EmailTransportSuccessTest() throws Exception{
        doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
        DummyEmailRequestDto dummyEmailRequestDto= new DummyEmailRequestDto("redped@ncaaver.com");
        mvc.perform(post("/api/email/auth")
                        .content(objectMapper.writeValueAsString(dummyEmailRequestDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));

    }
    @Test
    @DisplayName("이메일 전송 테스트(잘못된 값이 들어왔을 시)")
    public void EmailTransportRegexFailTest() throws Exception{
        doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
        DummyEmailRequestDto dummyEmailRequestDto= new DummyEmailRequestDto("redped.com");
        mvc.perform(post("/api/email/auth")
                        .content(objectMapper.writeValueAsString(dummyEmailRequestDto))
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcRestDocumentation.document("{ClassName}/{methodName}",
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value("이메일 제약조건"))
                        ),
                        responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER).description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지")
                        )
                ));

    }
}

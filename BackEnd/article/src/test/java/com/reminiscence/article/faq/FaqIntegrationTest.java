package com.reminiscence.article.faq;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
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

import java.sql.SQLException;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@Transactional
public class FaqIntegrationTest {

    @Autowired
    WebApplicationContext applicationContext;
    MockMvc mvc;
    @Autowired
    private Environment env;


    @BeforeEach
    public void init(RestDocumentationContextProvider restDocumentation) throws SQLException {
        mvc= MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation).operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build();
    }

    @Test
    @DisplayName("자주 묻는 질문 리스트 조회")
    public void getFaqListSuccessTest() throws Exception {
        mvc.perform(get("/api/faq?page=3")
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
                                fieldWithPath("faqVoList").type(JsonFieldType.ARRAY).description("FAQ 리스트"),
                                fieldWithPath("faqVoList[].id").type(JsonFieldType.NUMBER).description("FAQ 글번호"),
                                fieldWithPath("faqVoList[].question").type(JsonFieldType.STRING).description("질문"),
                                fieldWithPath("faqVoList[].answer").type(JsonFieldType.STRING).description("질문의 응답")

                        )
                ));
    }

}

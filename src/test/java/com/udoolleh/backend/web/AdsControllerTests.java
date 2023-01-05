package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.entity.Restaurant;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.AdsService;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.repository.RestaurantRepository;
import com.udoolleh.backend.web.dto.ResponseAds;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AdsControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AdsService adsService;

    @MockBean
    private AdminAuthenticationService adminAuthenticationService;

    @MockBean
    private JwtAuthTokenProvider jwtAuthTokenProvider;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void registerAdTest() throws Exception{
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());

        String adminAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        given(jwtAuthTokenProvider.resolveToken(any())).willReturn(Optional.of(adminAccessToken));
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);
        doNothing().when(adsService).registerAds(mockMultipartfile);

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/ad")
                .file(mockMultipartfile)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("ad-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-auth-token").description("어드민 액세스 토큰")
                        ),
                        requestParts(partWithName("file").description("사진 파일")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    void getAdsTest() throws Exception {
        ResponseAds.AdsDto responseAds = ResponseAds.AdsDto.builder()
                .id("64867f63-e88a-43e6-e57f-6d1323d70c0f")
                .photo("https://udo-photo-bucket.s3.ap-northeast-2.amazonaws.com/restaurant/a6cd7f6a-86f2-4771-a46a-125040da3327%ED%95%B4%EB%85%80%EC%B4%8C%ED%95%B4%EC%82%B0%EB%AC%BC2.png")
                .build();
        given(adsService.getAds()).willReturn(List.of(responseAds));

        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/ad")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("ad-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].id").type(JsonFieldType.STRING).description("광고 사진 아이디"),
                                        fieldWithPath("list.[].photo").type(JsonFieldType.STRING).description("이름")
                                )
                        )
                )
        ;
    }
}

package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.provider.security.JwtAuthTokenProvider;
import com.udoolleh.backend.provider.service.AdminAuthenticationService;
import com.udoolleh.backend.provider.service.TravelPlaceService;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.RequestPlace;
import com.udoolleh.backend.web.dto.ResponsePlace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Optional;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class PlaceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TravelPlaceService travelPlaceService;

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
    void getCourseTest() throws Exception {
        //given
        ResponsePlace.PlaceDto dto = ResponsePlace.PlaceDto.builder()
                .id(10L)
                .placeName("우도밭담")
                .photo("https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixabay.com%2Fko%2Fimages%2Fsearch%2Furl%2F&psig=AOvVaw2Kc0QFHfJJxEZtD_UxzWMD&ust=1671882640838000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLCV9ajWj_wCFQAAAAAdAAAAABAE")
                .intro("정겨운 농사와 어루어진 현무암의 밭담")
                .context("밭담은 현무암으로 구성되어 있다.")
                .gps(List.of(ResponsePlace.GpsDto.builder()
                        .latitude(12.123123)
                        .longitude(31.23231)
                        .build()))
                .build();

        given(travelPlaceService.getPlaceList()).willReturn(List.of(dto));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/place")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("place-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].id").type(JsonFieldType.NUMBER).description("장소 아이디"),
                                        fieldWithPath("list.[].placeName").type(JsonFieldType.STRING).description("장소 이름"),
                                        fieldWithPath("list.[].intro").type(JsonFieldType.STRING).description("인트로"),
                                        fieldWithPath("list.[].context").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("list.[].photo").type(JsonFieldType.STRING).description("사진"),
                                        fieldWithPath("list.[].gps.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("list.[].gps.[].longitude").type(JsonFieldType.NUMBER).description("경도")
                                )
                        )
                )
        ;
    }

    @Test
    void getCourseDetailTest() throws Exception {
        //given
        ResponsePlace.PlaceDto dto = ResponsePlace.PlaceDto.builder()
                .id(10L)
                .placeName("우도밭담")
                .photo("https://www.google.com/url?sa=i&url=https%3A%2F%2Fpixabay.com%2Fko%2Fimages%2Fsearch%2Furl%2F&psig=AOvVaw2Kc0QFHfJJxEZtD_UxzWMD&ust=1671882640838000&source=images&cd=vfe&ved=0CBAQjRxqFwoTCLCV9ajWj_wCFQAAAAAdAAAAABAE")
                .intro("정겨운 농사와 어루어진 현무암의 밭담")
                .context("밭담은 현무암으로 구성되어 있다.")
                .gps(List.of(ResponsePlace.GpsDto.builder()
                        .latitude(12.123123)
                        .longitude(31.23231)
                        .build()))
                .build();

        given(travelPlaceService.getPlaceDetail(10L)).willReturn(dto);

        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/place/{id}", 10)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("place-detail-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                pathParameters(parameterWithName("id").description("추천 관광지 아이디")),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.id").type(JsonFieldType.NUMBER).description("추천 관광지 아이디"),
                                        fieldWithPath("list.placeName").type(JsonFieldType.STRING).description("추천 관광지 이름"),
                                        fieldWithPath("list.intro").type(JsonFieldType.STRING).description("인트로"),
                                        fieldWithPath("list.context").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("list.photo").type(JsonFieldType.STRING).description("사진"),
                                        fieldWithPath("list.gps.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("list.gps.[].longitude").type(JsonFieldType.NUMBER).description("경도")
                                )
                        )
                )
        ;
    }

    @Test
    void registerPlaceTest() throws Exception {
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());

        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"placeName\": \"rodem garden\",\"intro\": \"Friendly Batdam\",\"context\": \"Batdam is basalt\",\"gps\" : [{\"latitude\" : 34.1232131,\"longitude\" : 127.1231231}]}".getBytes());

        String adminAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        given(jwtAuthTokenProvider.resolveToken(any())).willReturn(Optional.of(adminAccessToken));
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);
        doNothing().when(travelPlaceService).registerPlace(mockMultipartfile, new RequestPlace.RegisterPlaceDto().builder()
                .placeName("우도 밭담")
                .intro("정겨운 농사와 어루어진 현무암의 밭담")
                .context("밭담은 현무암으로 구성되어 있다.")
                .gps(List.of(RequestPlace.GpsDto.builder().latitude(12.121).longitude(31.332).build()))
                .build());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/place")
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("place-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-auth-token").description("어드민 액세스 토큰")
                        ),
                        requestParts(partWithName("file").description("사진 파일"),
                                partWithName("requestDto").description("관광지 정보")),
                        requestPartFields("requestDto",
                                fieldWithPath("placeName").type(JsonFieldType.STRING).description("관광지 이름"),
                                fieldWithPath("intro").type(JsonFieldType.STRING).description("소제목"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("설명"),
                                fieldWithPath("gps.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("gps.[].longitude").type(JsonFieldType.NUMBER).description("경도")
                                ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    void updatePlaceTest() throws Exception {
        MockMultipartFile mockMultipartfile = new MockMultipartFile("file", "test2.png",
                "image/png", "test data".getBytes());

        MockMultipartFile requestDto = new MockMultipartFile("requestDto", "",
                "application/json", "{\"placeName\": \"Batdam\",\"intro\": \"Friendly Batdam\",\"context\": \"Batdam is basalt\",\"gps\" : [{\"latitude\" : 34.1232131,\"longitude\" : 127.1231231}]}".getBytes());

        String adminAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        given(jwtAuthTokenProvider.resolveToken(any())).willReturn(Optional.of(adminAccessToken));
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);
        doNothing().when(travelPlaceService).updatePlace(mockMultipartfile,10L, new RequestPlace.UpdatePlaceDto().builder()
                .placeName("우도 밭담")
                .intro("정겨운 농사와 어루어진 현무암의 밭담")
                .context("밭담은 현무암으로 구성되어 있다.")
                .gps(List.of(RequestPlace.GpsDto.builder().latitude(12.121).longitude(31.332).build()))
                .build());

        mockMvc.perform(RestDocumentationRequestBuilders
                .multipart("/place/{id}", 10L)
                .file(mockMultipartfile)
                .file(requestDto)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("place-update-post",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-auth-token").description("어드민 액세스 토큰")
                        ),
                        pathParameters(parameterWithName("id").description("관광지 아이디")),
                        requestParts(partWithName("file").description("사진 파일"),
                                partWithName("requestDto").description("관광지 정보")),
                        requestPartFields("requestDto",
                                fieldWithPath("placeName").type(JsonFieldType.STRING).description("관광지 이름"),
                                fieldWithPath("intro").type(JsonFieldType.STRING).description("소제목"),
                                fieldWithPath("context").type(JsonFieldType.STRING).description("설명"),
                                fieldWithPath("gps.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                fieldWithPath("gps.[].longitude").type(JsonFieldType.NUMBER).description("경도")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }

    @Test
    void deletePlaceTest() throws Exception {
        String adminAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTY3MTc3NDI2NH0.b-02-QeknnbtWV1lrtOdXEYD9xYLLIQ3G0vIy_U8_-8";

        given(jwtAuthTokenProvider.resolveToken(any())).willReturn(Optional.of(adminAccessToken));
        given(adminAuthenticationService.validAdminToken(any())).willReturn(true);
        doNothing().when(travelPlaceService).deletePlace(10L);

        mockMvc.perform(RestDocumentationRequestBuilders
                .delete("/place/{id}", 10L)
                .header("x-auth-token", adminAccessToken)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo(document("place-delete",
                        preprocessRequest(modifyUris()
                                .scheme("http")
                                .host("ec2-43-200-118-169.ap-northeast-2.compute.amazonaws.com")
                                .removePort(), prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                                headerWithName("x-auth-token").description("어드민 액세스 토큰")
                        ),
                        pathParameters(parameterWithName("id").description("관광지 아이디")),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("api response 고유 아이디 값"),
                                fieldWithPath("dateTime").type(JsonFieldType.STRING).description("response 응답 시간"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("결과메세지"),
                                fieldWithPath("list").type(JsonFieldType.NULL).description("응답 데이터")
                        )
                ));
    }
}

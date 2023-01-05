package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.entity.TravelPlace;
import com.udoolleh.backend.provider.service.TravelPlaceService;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.repository.TravelPlaceRepository;
import com.udoolleh.backend.web.dto.ResponsePlace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class PlaceControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TravelPlaceRepository travelPlaceRepository;

    @Autowired
    private GpsRepository gpsRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TravelPlaceService travelPlaceService;

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



}

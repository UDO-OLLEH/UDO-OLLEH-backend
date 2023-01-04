package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.provider.service.CourseService;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.web.dto.ResponseCourse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import org.springframework.restdocs.payload.JsonFieldType;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ExtendWith(RestDocumentationExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class CourseControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private CourseService courseService;

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
        ResponseCourse.CourseDetailDto courseDetailTitleDto = ResponseCourse.CourseDetailDto.builder()
                .context("하우목동항")
                .type(CourseDetailType.TITLE)
                .build();
        ResponseCourse.CourseDetailDto courseDetailPhotoDto = ResponseCourse.CourseDetailDto.builder()
                .context("https://udo-photo-bucket.s3.ap-northeast-2.amazonaws.com/restaurant/a6cd7f6a-86f2-4771-a46a-125040da3327%ED%95%B4%EB%85%80%EC%B4%8C%ED%95%B4%EC%82%B0%EB%AC%BC2.png")
                .type(CourseDetailType.PHOTO)
                .build();
        ResponseCourse.CourseDetailDto courseDetailTextDto = ResponseCourse.CourseDetailDto.builder()
                .context("배를타고 처음 도착하면 하우목동항에 내리게 됩니다.")
                .type(CourseDetailType.TEXT)
                .build();
        ResponseCourse.GpsDto gpsDto = ResponseCourse.GpsDto.builder()
                .latitude(33.5153)
                .longitude(126.9681)
                .build();

        ResponseCourse.CourseDto courseDto = ResponseCourse.CourseDto.builder()
                .course("하우목동항-전기차 대여-비양도 캠핑장")
                .courseName("캠핑장 코스")
                .detail(List.of(courseDetailPhotoDto, courseDetailTextDto, courseDetailTitleDto))
                .gps(List.of(gpsDto, gpsDto, gpsDto))
                .id(2L)
                .build();
        given(courseService.getCourseList()).willReturn(List.of(courseDto));

        //when
        mockMvc.perform(RestDocumentationRequestBuilders
                .get("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andDo( // rest docs 문서 작성 시작
                        document("course-get", // 문서 조각 디렉토리 명
                                preprocessRequest(modifyUris()
                                        .scheme("http")
                                        .host("ec2-54-241-190-224.us-west-1.compute.amazonaws.com")
                                        .removePort(), prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields( // response 필드 정보 입력
                                        fieldWithPath("id").type(JsonFieldType.STRING).description("응답 아이디"),
                                        fieldWithPath("dateTime").type(JsonFieldType.STRING).description("응답 시간"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                                        fieldWithPath("list.[].id").type(JsonFieldType.NUMBER).description("여행지 코스 아이디"),
                                        fieldWithPath("list.[].courseName").type(JsonFieldType.STRING).description("코스 이름"),
                                        fieldWithPath("list.[].course").type(JsonFieldType.STRING).description("코스 경로"),
                                        fieldWithPath("list.[].detail.[].type").type(JsonFieldType.STRING).description("타입"),
                                        fieldWithPath("list.[].detail.[].context").type(JsonFieldType.STRING).description("내용"),
                                        fieldWithPath("list.[].gps.[].latitude").type(JsonFieldType.NUMBER).description("위도"),
                                        fieldWithPath("list.[].gps.[].longitude").type(JsonFieldType.NUMBER).description("경도")
                                        )
                        )
                )
        ;
    }
}

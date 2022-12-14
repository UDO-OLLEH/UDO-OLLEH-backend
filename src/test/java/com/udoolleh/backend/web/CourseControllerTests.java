package com.udoolleh.backend.web;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.Ads;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.repository.AdsRepository;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    private TravelCourseRepository travelCourseRepository;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private GpsRepository gpsRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

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
        TravelCourse travelCourse = TravelCourse.builder()
                .courseName("여행지 코스")
                .course("출발지-도착지")
                .build();
        travelCourseRepository.save(travelCourse);

        Gps gps = Gps.builder()
                .travelCourse(travelCourse)
                .longitude(12.111)
                .latitude(12.112)
                .build();
        gps = gpsRepository.save(gps);

        CourseDetail detail = CourseDetail.builder()
                .travelCourse(travelCourse)
                .type(CourseDetailType.TEXT)
                .context("내용")
                .build();
        detail = courseDetailRepository.save(detail);

        travelCourse.addDetail(detail);
        travelCourse.addGps(gps);

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

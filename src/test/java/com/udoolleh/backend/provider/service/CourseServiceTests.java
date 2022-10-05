package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.web.dto.RequestCourse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class CourseServiceTests {
    @Autowired
    private CourseService courseService;
    @Autowired
    private TravelCourseRepository travelCourseRepository;
    @Autowired
    private GpsRepository gpsRepository;
    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Test
    @DisplayName("여행지 코스 등록 테스트(성공)")
    void registerCourseTest(){
        List<RequestCourse.detailDto> detail = new ArrayList<>();
        detail.add(RequestCourse.detailDto.builder()
                .type(CourseDetailType.TEXT)
                .context("여기여기")
                .build());

        List<RequestCourse.gpsDto> gps = new ArrayList<>();
        gps.add(RequestCourse.gpsDto.builder()
                .latitude(34.12313)
                .longitude(127.342324)
                .build());

        RequestCourse.registerDto requestDto = RequestCourse.registerDto.builder()
                .courseName("우도 여행")
                .course("선착장 - 식당 - 올레길")
                .detail(detail)
                .gps(gps)
                .build();
        //여행지 코스 등록
        courseService.registerCourse(requestDto);

        assertNotNull(travelCourseRepository.findByCourseName(requestDto.getCourseName()));
        TravelCourse course = travelCourseRepository.findByCourseName(requestDto.getCourseName());
        assertNotNull(course.getDetailList());
        assertNotNull(course.getGpsList());
        }


}

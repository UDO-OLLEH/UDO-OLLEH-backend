package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseCourse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

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
        List<RequestCourse.DetailDto> detail = new ArrayList<>();
        detail.add(RequestCourse.DetailDto.builder()
                .type(CourseDetailType.TEXT)
                .context("여기여기")
                .build());

        List<RequestCourse.GpsDto> gps = new ArrayList<>();
        gps.add(RequestCourse.GpsDto.builder()
                .latitude(34.12313)
                .longitude(127.342324)
                .build());

        RequestCourse.RegisterDto requestDto = RequestCourse.RegisterDto.builder()
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

        @Test
        @DisplayName("여행지 코스 조회 테스트(성공)")
        void getCourseTest(){
        //등록
            TravelCourse course = TravelCourse.builder()
                    .courseName("우도 여행")
                    .course("선착장 - 식당 - 올레길")
                    .build();
            course = travelCourseRepository.save(course);

            Gps gps = Gps.builder()
                    .latitude(34.12311)
                    .longitude(127.3423423)
                    .travelCourse(course)
                    .build();
            gpsRepository.save(gps);

            Gps gps1 = Gps.builder()
                    .latitude(34.12311)
                    .longitude(127.3423423)
                    .travelCourse(course)
                    .build();
            gpsRepository.save(gps1);

            CourseDetail detail = CourseDetail.builder()
                    .type(CourseDetailType.TEXT)
                    .context("우도우도")
                    .travelCourse(course)
                    .build();
            courseDetailRepository.save(detail);

            course.addGps(gps);
            course.addGps(gps1);
            course.addDetail(detail);
            TravelCourse course1 = TravelCourse.builder()
                    .courseName("우도 여행")
                    .course("선착장 - 식당 - 올레길")
                    .build();
            course1 = travelCourseRepository.save(course1);
            Gps gps2 = Gps.builder()
                    .latitude(34.12311)
                    .longitude(127.3423423)
                    .travelCourse(course1)
                    .build();
            gpsRepository.save(gps2);

            CourseDetail detail1 = CourseDetail.builder()
                    .type(CourseDetailType.TEXT)
                    .context("우도우도")
                    .travelCourse(course1)
                    .build();
            courseDetailRepository.save(detail1);


            course1.addGps(gps2);
            course1.addDetail(detail1);


            //조회
            List<ResponseCourse.GetDto> list = courseService.getCourses();
            assertNotNull(list);
        }

}

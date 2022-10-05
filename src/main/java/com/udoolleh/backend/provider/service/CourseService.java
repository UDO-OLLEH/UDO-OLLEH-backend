package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.CourseServiceInterface;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.exception.errors.TravelCourseDuplicatedException;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.web.dto.RequestCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService implements CourseServiceInterface {
    private final TravelCourseRepository travelCourseRepository;
    private final GpsRepository gpsRepository;
    private final CourseDetailRepository courseDetailRepository;

    @Override
    @Transactional
    public void registerCourse(RequestCourse.registerDto requestDto){
        TravelCourse course = travelCourseRepository.findByCourseName(requestDto.getCourseName());
        if(course != null){ //코스명이 중복됐을 경우
            throw new TravelCourseDuplicatedException();
        }
        course = TravelCourse.builder()
                .courseName(requestDto.getCourseName())
                .course(requestDto.getCourse())
                .build();
        course = travelCourseRepository.save(course);

        for(RequestCourse.detailDto item : requestDto.getDetail()){
            CourseDetail detail = CourseDetail.builder()
                    .type(item.getType())
                    .context(item.getContext())
                    .travelCourse(course)
                    .build();
            detail = courseDetailRepository.save(detail);
            course.addDetail(detail);
        }

        for(RequestCourse.gpsDto item : requestDto.getGps()){
            Gps gps = Gps.builder()
                    .latitude(item.getLatitude())
                    .longitude(item.getLongitude())
                    .travelCourse(course)
                    .build();
            gps = gpsRepository.save(gps);
            course.addGps(gps);
        }
    }
}

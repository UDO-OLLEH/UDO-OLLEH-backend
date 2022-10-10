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
import com.udoolleh.backend.web.dto.ResponseCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements CourseServiceInterface {
    private final TravelCourseRepository travelCourseRepository;
    private final GpsRepository gpsRepository;
    private final CourseDetailRepository courseDetailRepository;

    @Override
    @Transactional
    public void registerCourse(RequestCourse.RegisterDto requestDto){
        TravelCourse course = travelCourseRepository.findByCourseName(requestDto.getCourseName());
        if(course != null){ //코스명이 중복됐을 경우
            throw new TravelCourseDuplicatedException();
        }
        course = TravelCourse.builder()
                .courseName(requestDto.getCourseName())
                .course(requestDto.getCourse())
                .build();
        course = travelCourseRepository.save(course);

        for(RequestCourse.DetailDto item : requestDto.getDetail()){
            CourseDetail detail = CourseDetail.builder()
                    .type(item.getType())
                    .context(item.getContext())
                    .travelCourse(course)
                    .build();
            detail = courseDetailRepository.save(detail);
            course.addDetail(detail);
        }

        for(RequestCourse.GpsDto item : requestDto.getGps()){
            Gps gps = Gps.builder()
                    .latitude(item.getLatitude())
                    .longitude(item.getLongitude())
                    .travelCourse(course)
                    .build();
            gps = gpsRepository.save(gps);
            course.addGps(gps);
        }
    }

    @Override
    @Transactional
    public List<ResponseCourse.GetDto> getCourses(){
        List<ResponseCourse.GetDto> response = new ArrayList<>();
        List<ResponseCourse.GpsDto> gpsList = new ArrayList<>();
        List<ResponseCourse.DetailDto> detailList = new ArrayList<>();
        List<TravelCourse> courseList = travelCourseRepository.findAllCourse();

        for(TravelCourse item : courseList){
            for(Gps gps : item.getGpsList()){
                gpsList.add(ResponseCourse.GpsDto.of(gps));
            }
            for(CourseDetail detail : item.getDetailList()){
                detailList.add(ResponseCourse.DetailDto.of(detail));
            }

            ResponseCourse.GetDto dto = ResponseCourse.GetDto.builder()
                    .courseName(item.getCourseName())
                    .course(item.getCourse())
                    .detail(detailList)
                    .gps(gpsList)
                    .build();
            response.add(dto);
        }
        return response;
    }
}

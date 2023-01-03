package com.udoolleh.backend.provider.service;

import com.udoolleh.backend.core.service.CourseServiceInterface;
import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import com.udoolleh.backend.entity.TravelCourse;
import com.udoolleh.backend.exception.CustomException;
import com.udoolleh.backend.exception.ErrorCode;
import com.udoolleh.backend.repository.CourseDetailRepository;
import com.udoolleh.backend.repository.GpsRepository;
import com.udoolleh.backend.repository.TravelCourseRepository;
import com.udoolleh.backend.web.dto.RequestCourse;
import com.udoolleh.backend.web.dto.ResponseCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService implements CourseServiceInterface {
    private final TravelCourseRepository travelCourseRepository;
    private final GpsRepository gpsRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final S3Service s3Service;

    @Override
    @Transactional
    public void registerCourse(RequestCourse.RegisterCourseDto requestDto) {
        TravelCourse course = travelCourseRepository.findByCourseName(requestDto.getCourseName());
        if (course != null) { //코스명이 중복됐을 경우
            throw new CustomException(ErrorCode.TRAVEL_COURSE_DUPLICATED);
        }
        course = TravelCourse.builder()
                .courseName(requestDto.getCourseName())
                .course(requestDto.getCourse())
                .build();
        course = travelCourseRepository.save(course);

        if (requestDto.getDetail() != null) {
            for (RequestCourse.DetailDto item : requestDto.getDetail()) {
                CourseDetail detail = CourseDetail.builder()
                        .type(item.getType())
                        .context(item.getContext())
                        .travelCourse(course)
                        .build();
                detail = courseDetailRepository.save(detail);
                course.addDetail(detail);
            }
        }

        if (requestDto.getGps() != null) {
            for (RequestCourse.GpsDto item : requestDto.getGps()) {
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

    @Override
    @Transactional(readOnly = true)
    public List<ResponseCourse.CourseDto> getCourseList() {
        List<ResponseCourse.CourseDto> response = new ArrayList<>();
        List<ResponseCourse.GpsDto> gpsList = new ArrayList<>();
        List<ResponseCourse.CourseDetailDto> detailList = new ArrayList<>();
        List<TravelCourse> courseList = travelCourseRepository.findAllCourse();

        for (TravelCourse item : courseList) {
            for (Gps gps : item.getGpsList()) {
                gpsList.add(ResponseCourse.GpsDto.of(gps));
            }
            for (CourseDetail detail : item.getDetailList()) {
                detailList.add(ResponseCourse.CourseDetailDto.of(detail));
            }

            ResponseCourse.CourseDto dto = ResponseCourse.CourseDto.builder()
                    .id(item.getId())
                    .courseName(item.getCourseName())
                    .course(item.getCourse())
                    .detail(detailList)
                    .gps(gpsList)
                    .build();
            response.add(dto);
        }
        return response;
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        TravelCourse course = travelCourseRepository.findCourse(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL_COURSE));

        //s3에서 사진 삭제
        for (CourseDetail detail : course.getDetailList()) {
            if (detail.getType().equals(CourseDetailType.PHOTO)) {
                s3Service.deleteFile(detail.getContext());
            }
        }
        travelCourseRepository.delete(course);
    }
}

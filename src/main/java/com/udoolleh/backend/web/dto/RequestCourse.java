package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.CourseDetailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RequestCourse {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterCourseDto{
        @NotEmpty(message = "코스명이 비어있습니다.")
        private String courseName;
        @NotEmpty(message = "코스가 비어있습니다.")
        private String course;
        private List<DetailDto> detail;
        private List<GpsDto> gps;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailDto{
        @NotEmpty(message = "타입이 비어있습니다.")
        private CourseDetailType type;
        @NotEmpty(message = "내용이 비어있습니다.")
        private String context;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GpsDto{
        @NotEmpty(message = "위도가 비어있습니다.")
        private Double latitude;
        @NotEmpty(message = "경도가 비어있습니다.")
        private Double longitude;
    }
}

package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.CourseDetailType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class RequestCourse {
    @Builder
    @Data
    public static class RegisterDto{
        @NotEmpty(message = "코스명이 비어있습니다.")
        private String courseName;
        @NotEmpty(message = "코스가 비어있습니다.")
        private String course;
        private List<DetailDto> detail;
        private List<GpsDto> gps;


    }

    @Builder
    @Data
    public static class DetailDto{
        private CourseDetailType type;
        private String context;
    }

    @Builder
    @Data
    public static class GpsDto{
        private Double latitude;
        private Double longitude;
    }
}

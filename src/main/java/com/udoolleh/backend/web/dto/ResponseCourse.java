package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public class ResponseCourse {
    @Builder
    @Getter
    public static class CourseDto{
        private Long id;
        private String courseName;
        private String course;
        private List<CourseDetailDto> detail;
        private List<GpsDto> gps;

    }

    @Builder
    @Getter
    public static class CourseDetailDto{
        private CourseDetailType type;
        private String context;

        public static CourseDetailDto of(CourseDetail detail){
            return CourseDetailDto.builder()
                    .type(detail.getType())
                    .context(detail.getContext())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class GpsDto{
        private Double latitude;
        private Double longitude;

        public static GpsDto of(Gps gps){
            return GpsDto.builder()
                    .latitude(gps.getLatitude())
                    .longitude(gps.getLongitude())
                    .build();
        }
    }
}

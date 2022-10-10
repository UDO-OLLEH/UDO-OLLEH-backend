package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.CourseDetailType;
import com.udoolleh.backend.entity.CourseDetail;
import com.udoolleh.backend.entity.Gps;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ResponseCourse {
    @Builder
    @Data
    public static class GetDto{
        private String courseName;
        private String course;
        private List<DetailDto> detail;
        private List<GpsDto> gps;

    }

    @Builder
    @Data
    public static class DetailDto{
        private CourseDetailType type;
        private String context;

        public static DetailDto of(CourseDetail detail){
            return DetailDto.builder()
                    .type(detail.getType())
                    .context(detail.getContext())
                    .build();
        }
    }

    @Builder
    @Data
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

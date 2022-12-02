package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Gps;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

public class ResponsePlace {

    @Builder
    @Data
    public static class PlaceDto {
        private Long id;
        private String placeName;
        private String intro;
        private String context;
        private String photo;
        private List<GpsDto> gps;
    }

    @Builder
    @Getter
    public static class GpsDto {
        private Double latitude;
        private Double longitude;

        public static GpsDto of(Gps gps) {
            return GpsDto.builder()
                    .latitude(gps.getLatitude())
                    .longitude(gps.getLongitude())
                    .build();
        }
    }
}

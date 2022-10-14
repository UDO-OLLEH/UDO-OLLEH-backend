package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.TravelPlace;
import lombok.Builder;
import lombok.Data;

public class ResponsePlace {

    @Builder
    @Data
    public static class ListPlaceDto {
        private Long id;
        private String placeName;
        private String context;

    }
}

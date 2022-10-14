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

        public static ListPlaceDto of(TravelPlace travelPlace) {
            return ListPlaceDto.builder()
                    .id(travelPlace.getId())
                    .placeName(travelPlace.getPlaceName())
                    .context(travelPlace.getContext())
                    .build();
        }

    }
}

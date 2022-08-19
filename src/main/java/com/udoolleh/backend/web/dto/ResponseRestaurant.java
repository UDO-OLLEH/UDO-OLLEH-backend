package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.PlaceType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ResponseRestaurant {
    @Builder
    @Data
    public static class restaurantDto{
        private String name;
        private PlaceType placeType;
        private String category;
        private String address;
        private List<String> imagesUrl;
        private Float totalGrade;
    }
}

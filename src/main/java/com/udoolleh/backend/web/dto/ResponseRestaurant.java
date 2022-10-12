package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.PlaceType;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ResponseRestaurant {
    @Builder
    @Getter
    public static class RestaurantDto{
        private String id;
        private String name;
        private PlaceType placeType;
        private String category;
        private String address;
        private List<String> imagesUrl;
        private Double totalGrade;
        private String xCoordinate;
        private String yCoordinate;

    }
}

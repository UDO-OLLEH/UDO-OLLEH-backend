package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.PlaceType;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.Restaurant;
import java.util.stream.Collectors;
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

        public static RestaurantDto of(Restaurant restaurant){
            return RestaurantDto.builder()
                    .id(restaurant.getId())
                    .name(restaurant.getName())
                    .placeType(restaurant.getPlaceType())
                    .category(restaurant.getCategory())
                    .imagesUrl(restaurant.getPhotoList().stream()
                            .map(Photo::getUrl)
                            .collect(Collectors.toList()))
                    .address(restaurant.getAddress())
                    .totalGrade(restaurant.getTotalGrade())
                    .xCoordinate(restaurant.getXCoordinate())
                    .yCoordinate(restaurant.getYCoordinate())
                    .build();
        }
    }


}

package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.PlaceType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestRestaurant {
    @Builder
    @Data
    public static class registerDto{
        @NotNull(message = "식당 이름이 비어있습니다.")
        private String name;
        @NotNull(message = "식당 타입이 비어있습니다.")
        private PlaceType placeType;
        @NotNull(message = "식당 카테고리가 비어있습니다.")
        private String category;
        @NotNull(message = "식당 주소가 비어있습니다.")
        private String address;
    }
}

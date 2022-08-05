package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.core.type.PlaceType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class ResponseKakaoApi {
    @Builder
    @Data
    public static class PlaceDto{
        @NotEmpty(message = "장소 이름이 비어있습니다.")
        private String placeName;
        @NotEmpty(message = "장소 타입이 비어있습니다.")
        private PlaceType placeType;
        @NotEmpty(message = "카테고리가 비어있습니다.")
        private String category;
        @NotEmpty(message = "주소가 비어있습니다.")
        private String address;
        @NotEmpty(message = "페이지 정보가 비어있습니다.")
        private int page;
    }
}

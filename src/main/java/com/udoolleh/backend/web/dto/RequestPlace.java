package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

public class RequestPlace {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterPlaceDto {
        @NotEmpty(message = "여행지가 비어있습니다.")
        private String placeName;
        @NotEmpty(message = "설명이 비어있습니다.")
        private String context;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdatePlaceDto{
        @NotEmpty(message = "여행지가 비어있습니다.")
        private String placeName;
        @NotEmpty(message = "설명이 비어있습니다.")
        private String context;
    }
}

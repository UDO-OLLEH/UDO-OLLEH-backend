package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestReview {

    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "음식점 아이디가 비었습니다.")
        private String restaurantId;
        @NotEmpty(message = "내용이 비어있습니다.")
        private String context;
        @NotEmpty(message = "별점이 비었습니다.")
        private Double grade;
    }

    @Builder
    @Data
    public static class modifyDto{
        @NotEmpty(message = "내용이 비어있습니다")
        private String context;
        @NotEmpty(message = "별점이 비었습니다.")
        private Double grade;
    }
}

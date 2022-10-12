package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestReview {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterReviewDto{
        @NotEmpty(message = "음식점 이름이 비었습니다.")
        private String restaurantName;
        @NotEmpty(message = "내용이 비어있습니다.")
        private String context;
        @NotNull(message = "별점이 비었습니다.")
        private double grade;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewDto{
        @NotEmpty(message = "내용이 비어있습니다")
        private String context;
        @NotNull(message = "별점이 비었습니다.")
        private double grade;
    }
}

package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestReview {

    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "음식점 이름이 비었습니다.")
        private String restaurantName;
        @NotEmpty(message = "내용이 비어있습니다.")
        private String context;
        @NotNull(message = "별점이 비었습니다.")
        private double grade;
    }

    @Builder
    @Data
    public static class modifyDto{
        @NotEmpty(message = "내용이 비어있습니다")
        private String context;
        @NotNull(message = "별점이 비었습니다.")
        private double grade;
    }
}

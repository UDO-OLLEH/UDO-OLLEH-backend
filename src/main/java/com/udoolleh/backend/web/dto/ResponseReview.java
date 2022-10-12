package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

public class ResponseReview {
    @Builder
    @Getter
    public static class ReviewDto{
        private String reviewId;
        private String nickname;
        private String context;
        private String photo;
        private Double grade;
    }
}

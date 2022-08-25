package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseReview {
    @Builder
    @Data
    public static class getReviewDto{
        private String nickname;
        private String context;
        private String photo;
        private Double grade;
    }
}

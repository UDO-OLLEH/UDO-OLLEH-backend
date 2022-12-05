package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestAds {
    @Builder
    @Data
    public static class RegisterAdsDto {
        @NotEmpty(message = "설명이 비었습니다.")
        private String context;
    }
}

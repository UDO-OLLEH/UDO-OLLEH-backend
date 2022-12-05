package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

public class ResponseAds {
    @Builder
    @Getter
    public static class AdsDto{
        private String id;
        private String photo;
        private String context;
    }
}

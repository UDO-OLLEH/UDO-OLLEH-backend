package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseMenuDto {
    @Builder
    @Data
    public static class getMenu{
        private String name;
        private String photo;
        private Integer price;
        private String description;
    }
}

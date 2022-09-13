package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseMenu {
    @Builder
    @Data
    public static class getMenuDto{
        private String name;
        private String photo;
        private Integer price;
        private String description;
    }
}

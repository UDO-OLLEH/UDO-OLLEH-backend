package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class ResponseMenu {
    @Builder
    @Getter
    public static class MenuDto{
        private String name;
        private String photo;
        private Integer price;
        private String description;
    }
}

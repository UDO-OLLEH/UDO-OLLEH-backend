package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestMenu {

    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "가게이름이 비어있습니다.")
        private String restaurantName;
        @NotEmpty(message = "메뉴 이름이 비어있습니다.")
        private String name;
        @NotNull(message = "가격이 비어있습니다.")
        private int price;
        private String description;
    }
}

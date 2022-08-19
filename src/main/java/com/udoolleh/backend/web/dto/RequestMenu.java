package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class RequestMenu {

    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "가게 아이디가 비어있습니다.")
        private String restaurantId;
        @NotEmpty(message = "메뉴 이름이 비어있습니다.")
        private String name;
        @NotEmpty(message = "가격이 비어있습니다.")
        private Integer price;
        private String description;
    }
}

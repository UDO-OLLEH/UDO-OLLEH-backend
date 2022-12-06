package com.udoolleh.backend.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestShipFare {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterShipFareDto{
        @NotNull(message = "항구 아이디가 비었습니다.")
        private Long harborId;
        @NotEmpty(message = "연령대가 비었습니다.")
        private String ageGroup;
        @NotNull(message = "왕복 가격이 비었습니다.")
        private Integer roundTrip;
        @NotNull(message = "입도 가격이 비었습니다.")
        private Integer enterIsland;
        @NotNull(message = "출도 가격이 비었습니다.")
        private Integer leaveIsland;
    }
}

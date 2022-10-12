package com.udoolleh.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.udoolleh.backend.core.type.ShipCourseType;
import com.udoolleh.backend.core.type.ShipTimetableType;
import lombok.*;

import java.util.Date;
import java.util.List;

public class RequestWharfTimetable {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterWharfTimeDto{
        @NotEmpty(message = "선착장이 비었다")
        private ShipCourseType wharfCourse;
        @NotEmpty(message = "시간이 비었다")
        private List<String> departureTime;
        @NotEmpty(message = "월 타입이 비었다")
        private ShipTimetableType monthType;
    }
}

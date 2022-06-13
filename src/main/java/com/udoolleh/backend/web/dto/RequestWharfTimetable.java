package com.udoolleh.backend.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class RequestWharfTimetable {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StationTime {
        @NotNull(message = "선착장이 비었다")
        private String wharf;

        @NotNull(message = "시간이 비었다")
        @JsonFormat(pattern = "HH:mm:ss", timezone = "Asia/Seoul")
        private Date time;
    }
}
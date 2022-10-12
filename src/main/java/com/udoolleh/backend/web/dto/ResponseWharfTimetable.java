package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

public class ResponseWharfTimetable {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WharfTimetableDto{
        private String wharfCourse;
        private String monthType;
        private List<String> departureTime;
    }
}

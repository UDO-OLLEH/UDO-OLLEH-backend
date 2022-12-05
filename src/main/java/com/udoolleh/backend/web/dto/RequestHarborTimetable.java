package com.udoolleh.backend.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestHarborTimetable {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterHarborTimetableDto{
        private String harborName;
        private String destination;
        private String period;
        private String operatingTime;
    }
}

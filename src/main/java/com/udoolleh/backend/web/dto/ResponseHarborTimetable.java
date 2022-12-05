package com.udoolleh.backend.web.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

public class ResponseHarborTimetable {
    @Builder
    @Data
    public static class HarborTimetableDto {
        private String destination;
        private List<ResponseHarborTimetable.TimetableDto> timetableDto;
    }

    @Builder
    @Data
    public static class TimetableDto {
        private Long id;
        private String period;
        private String operatingTime;
    }
}

package com.udoolleh.backend.web.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

public class ResponseHarbor {
    @Builder
    @Data
    public static class HarborDto {
        private Long id;
        private String name;
    }
}

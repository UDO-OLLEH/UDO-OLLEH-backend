package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class ResponseBoard {
    @Builder
    @Data
    public static class Board{
        private long id;
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;

    }
}

package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

public class RequestBoard {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creates {
        private User user;
        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotEmpty(message = "내용이 없습니다.")
        private String context;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Updates {
        private User user;
        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotEmpty(message = "내용이 없습니다.")
        private String context;
    }
}

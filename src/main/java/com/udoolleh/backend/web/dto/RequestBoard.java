package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestBoard {
    @Builder
    @Data
    public static class Register{
        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotEmpty(message = "카테고리 설정이 되어있지 않습니다.")
        private String category;
        @NotEmpty(message = "내용이 없습니다.")
        private String content;
    }
    @Builder
    @Data
    public static class Update {
        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotEmpty(message = "카테고리 설정이 되어있지 않습니다.")
        private String category;
        @NotEmpty(message = "내용이 없습니다.")
        private String content;
    }
}

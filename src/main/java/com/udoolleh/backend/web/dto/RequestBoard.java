package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RequestBoard {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterBoardDto {

        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotNull(message = "해시태그가 없습니다.")
        private String hashtag; // 해시태크가 없으면 ""로 들어와야 함
        @NotEmpty(message = "내용이 없습니다.")
        private String context;

    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBoardDto {

        @NotEmpty(message = "제목이 비어있습니다.")
        private String title;
        @NotNull(message = "해시태그가 없습니다.")
        private String hashtag; // 해시태크가 없으면 ""로 들어와야 함
        @NotEmpty(message = "내용이 없습니다.")
        private String context;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CountDto{
        private Long countVisit;
    }
}

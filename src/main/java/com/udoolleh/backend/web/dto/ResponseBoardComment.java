package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

public class ResponseBoardComment {
    @Builder
    @Getter
    public static class BoardCommentDto {
        private String id;
        private String context;
        private String nickname;
        private String profile;
        private LocalDateTime createAt;
    }

}

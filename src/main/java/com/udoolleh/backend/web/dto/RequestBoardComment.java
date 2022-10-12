package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RequestBoardComment {
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterBoardCommentDto{
        @NotEmpty(message = "게시물 아이디가 비어있습니다.")
        private String boardId;
        @NotEmpty(message = "댓글 내용이 비었습니다.")
        private String context;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateBoardCommentDto{
        @NotEmpty(message = "댓글의 아이디가 비어있습니다.")
        private String commentId;
        @NotEmpty(message = "댓글 내용이 비었습니다.")
        private String context;
    }

}

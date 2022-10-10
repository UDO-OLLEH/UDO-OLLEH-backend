package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class RequestBoardComment {
    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "게시물 아이디가 비어있습니다.")
        private String boardId;
        @NotEmpty(message = "댓글 내용이 비었습니다.")
        private String context;
    }

}

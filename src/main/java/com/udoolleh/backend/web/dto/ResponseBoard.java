package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

public class ResponseBoard {

    @Builder
    @Data
    public static class ListBoard {
        private String title;
        private String context;
        private Date createAt;

        public static ListBoard of(Board board){
            return ListBoard.builder()
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .build();
        }

    }
}

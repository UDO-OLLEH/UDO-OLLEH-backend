package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class ResponseBoard {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailBoard{
        private String title;
        private String context;
        private Date createAt;
        private User user;
        //private Like like;
        //private Reply reply;
        //private Category category;


    }

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

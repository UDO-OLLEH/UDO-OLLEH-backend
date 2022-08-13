package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResponseBoard {
    @Builder
    @Data
    public static class BoardDetail{
        private String boardId;
        private User user;
        private String title;
        private String context;
        private Date createAt;

        //private Reply reply;
        //private Like like;
        //private Category category;
    }
    @Builder
    @Data
    public static class Board{
        private String title;
        private String context;
        private Date createAt;

        public static Board of(Board board){
            return Board.builder()
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .build();
        }
    }
}

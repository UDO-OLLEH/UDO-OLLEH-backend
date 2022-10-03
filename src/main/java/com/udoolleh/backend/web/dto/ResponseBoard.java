package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.User;
import lombok.*;

import java.util.Date;

public class ResponseBoard {

    @Getter
    public static class detailBoardDto {
        private String boardId;
        private String title;
        private String context;
        private String photo;
        private Date createAt;
        private User user;

        public detailBoardDto(Board board) {
            this.title = board.getTitle();
            this.context = board.getContext();
            this.photo = board.getPhoto();
            this.createAt = board.getCreateAt();
            this.user = board.getUser();
        }
        //private Like like;
        //private Reply reply;
        //private Category category;

    }

    @Builder
    @Data
    public static class listBoardDto {
        private String title;
        private String context;
        private Date createAt;

        public static listBoardDto of(Board board) {
            return listBoardDto.builder()
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .build();
        }

    }

    @Builder
    @Data
    public static class getLikeBoardDto {
        private String title;
        private String context;
        private Date createAt;

        public static getLikeBoardDto of(Board board) {
            return getLikeBoardDto.builder()
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .build();
        }

    }
}

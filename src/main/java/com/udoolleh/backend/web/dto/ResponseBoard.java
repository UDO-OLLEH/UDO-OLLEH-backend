package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.User;
import lombok.*;

import java.util.Date;

public class ResponseBoard {

    @Builder
    @Getter
    public static class detailBoardDto {

        private String id;
        private String title;
        private String context;
        private String photo;
        private Date createAt;
        private String nickname;
        private Long countLikes;
        //private Reply reply;
        //private Category category;

    }

    @Builder
    @Data
    public static class listBoardDto {
        private String title;
        private String context;
        private Date createAt;
        private long countVisit;
        private long countLikes;

        public static listBoardDto of(Board board) {
            return listBoardDto.builder()
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .countVisit(board.getCountVisit())
                    .countLikes(board.getCountLikes())
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

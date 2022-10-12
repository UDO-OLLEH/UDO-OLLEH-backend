package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.Board;
import com.udoolleh.backend.entity.Photo;
import com.udoolleh.backend.entity.User;
import lombok.*;

import java.util.Date;

public class ResponseBoard {

    @Builder
    @Getter
    public static class BoardDto {

        private String id;
        private String title;
        private String context;
        private String photo;
        private Date createAt;
        private String nickname;
        private Long countLikes;

    }

    @Builder
    @Getter
    public static class BoardListDto {
        private String id;
        private String title;
        private String context;
        private Date createAt;
        private Long countVisit;
        private Long countLikes;

        public static BoardListDto of(Board board) {
            return BoardListDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .countVisit(board.getCountVisit())
                    .countLikes(board.getCountLikes())
                    .build();
        }

    }

    @Builder
    @Getter
    public static class LikeBoardDto {
        private String id;
        private String title;
        private String context;
        private Date createAt;

        public static LikeBoardDto of(Board board) {
            return LikeBoardDto.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .context(board.getContext())
                    .createAt(board.getCreateAt())
                    .build();
        }

    }
}

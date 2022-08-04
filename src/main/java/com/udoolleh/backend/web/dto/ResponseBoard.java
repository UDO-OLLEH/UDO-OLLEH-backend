package com.udoolleh.backend.web.dto;

import com.udoolleh.backend.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

public class ResponseBoard {
    @Builder
    @Data
    public static class Board{
        private User user;
        private Long id;
        private String title;
        private String category;
        private String context;
        private Date createAt;
    }
}

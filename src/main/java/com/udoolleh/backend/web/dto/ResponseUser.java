package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

public class ResponseUser {
    @Builder
    @Data
    public static class Login{
        private String accessToken;
        private String refreshToken;
    }
    @Builder
    @Data
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }
}

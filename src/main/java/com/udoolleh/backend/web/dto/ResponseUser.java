package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class ResponseUser {
    @Builder
    @Getter
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    public static class UserDto {
        private String nickname;
        private String profileImage;
    }
}

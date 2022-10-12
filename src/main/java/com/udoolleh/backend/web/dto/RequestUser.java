package com.udoolleh.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

public class RequestUser {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterUserDto{
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "닉네임이 비어있습니다.")
        private String nickname;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateUserDto{
        @NotEmpty(message = "닉네임이 비어있습니다.")
        private String nickname;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
}

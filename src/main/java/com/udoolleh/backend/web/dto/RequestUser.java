package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestUser {

    @Builder
    @Data
    public static class registerDto{
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "닉네임이 비어있습니다.")
        private String nickname;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
    @Builder
    @Data
    public static class loginDto {
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
}

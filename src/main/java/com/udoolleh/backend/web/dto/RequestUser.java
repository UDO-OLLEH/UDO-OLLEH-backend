package com.udoolleh.backend.web.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

public class RequestUser {

    @Builder
    @Data
    public static class Register{
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "닉네임이 비어있습니다.")
        private String nickname;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
    @Builder
    @Data
    public static class Login {
        @NotEmpty(message = "이메일이 비어있습니다.")
        private String email;
        @NotEmpty(message = "비밀번호 입력이 되어있지 않습니다.")
        private String password;
    }
}

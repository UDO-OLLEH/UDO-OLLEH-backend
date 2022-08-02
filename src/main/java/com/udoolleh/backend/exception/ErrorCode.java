package com.udoolleh.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND_PATH(HttpStatus.NOT_FOUND, "PATH_001", "NOT FOUND PATH"), // 없는 경로로 요청보낸 경우
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"PATH_002","METHOD NOT ALLOWED"), // POST GET방식 잘못 보낸경우
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "PATH_003", "UNSUPPORTED MEDIA TYPE"),


    LOGIN_FAILED(HttpStatus.NOT_FOUND, "AUTH_002", " LOGIN_FAILED."),
    REGISTER_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_001", " AUTHENTICATION_FAILED."),
    REQUEST_PARAMETER_BIND_FAILED(HttpStatus.BAD_REQUEST, "REQ_001", "PARAMETER_BIND_FAILED"),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH004", "INVALID_JWT_TOKEN."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH001", " AUTHENTICATION_FAILED."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER_001", "NOT_FOUND_USER"),
    WHARF_NAME_DUPLICATED(HttpStatus.FORBIDDEN, "WHARF_001", "선착장 이름이 중복됨"),
    NOT_FOUND_WHARF(HttpStatus.NOT_FOUND, "WHARF_002", "존재하지 않는 선착장"),
    WHARF_TIME_DUPLICATED(HttpStatus.FORBIDDEN, "WHARF_003", "배 출발 시간이 중복됨"),
    NOT_FOUND_WHARF_TIMETABLE(HttpStatus.NOT_FOUND, "WHARF_004", "배 시간이 존재하지 않음"),
    REVIEW_DUPLICATED(HttpStatus.FORBIDDEN, "REVIEW_001", "REVIEW_DUPLICATED"),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, "WHARF_004", "NOT_FOUND_RESTAURANT");


    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

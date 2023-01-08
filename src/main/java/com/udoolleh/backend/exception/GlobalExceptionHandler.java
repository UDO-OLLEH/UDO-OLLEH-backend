package com.udoolleh.backend.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //코드 중복을 줄이기 위한 에러 핸들러 메서드
    private ResponseEntity<ErrorResponse> handleException(Exception e, ErrorCode errorCode) {

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().name())
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    /**
     * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleParamViolationException(BindException e) {
        // 파라미터 validation에 걸렸을 경우
        return handleException(e, ErrorCode.REQUEST_PARAMETER_BIND_FAILED);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        // 없는 경로로 요청 시
        return handleException(e, ErrorCode.NOT_FOUND_PATH);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        // GET POST 방식이 잘못된 경우
        return handleException(e, ErrorCode.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        //Content-Type 이 잘못된 경우
        return handleException(e, ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

}



package com.udoolleh.backend.exception;


import com.udoolleh.backend.entity.Menu;
import com.udoolleh.backend.exception.errors.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleParamViolationException(BindException ex) {
        // 파라미터 validation에 걸렸을 경우

        ErrorResponse response = ErrorResponse.builder()
                .status(ErrorCode.REQUEST_PARAMETER_BIND_FAILED.getStatus().value())
                .message(ErrorCode.REQUEST_PARAMETER_BIND_FAILED.getMessage())
                .code(ErrorCode.REQUEST_PARAMETER_BIND_FAILED.getCode())
                .build();
        return new ResponseEntity<>(response, ErrorCode.REQUEST_PARAMETER_BIND_FAILED.getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        // 없는 경로로 요청 시

        ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.NOT_FOUND_PATH.getMessage())
                .status(ErrorCode.NOT_FOUND_PATH.getStatus().value())
                .code(ErrorCode.NOT_FOUND_PATH.getCode())
                .build();

        return new ResponseEntity<>(response, ErrorCode.NOT_FOUND_PATH.getStatus());
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        // GET POST 방식이 잘못된 경우

        ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.METHOD_NOT_ALLOWED.getMessage())
                .status(ErrorCode.METHOD_NOT_ALLOWED.getStatus().value())
                .code(ErrorCode.METHOD_NOT_ALLOWED.getCode())
                .build();

        return new ResponseEntity<>(response, ErrorCode.METHOD_NOT_ALLOWED.getStatus());
    }
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {

        ErrorResponse response = ErrorResponse.builder()
                .message(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage())
                .status(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getStatus().value())
                .code(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode())
                .build();

        return new ResponseEntity<>(response, ErrorCode.UNSUPPORTED_MEDIA_TYPE.getStatus());
    }
    @ExceptionHandler(WharfNameDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleWharfNameDuplicatedException(WharfNameDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.WHARF_NAME_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(NotFoundWharfException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundWharfException(NotFoundWharfException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_WHARF;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(WharfTimeDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleWharfTimeDuplicatedException(WharfTimeDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.WHARF_TIME_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(NotFoundWharfTimetableException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundWharfTimetableException(NotFoundWharfTimetableException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_WHARF_TIMETABLE;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundUserException(NotFoundUserException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_USER;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(NotFoundRestaurantException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundRestaurantException(NotFoundRestaurantException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_RESTAURANT;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(ReviewDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleReviewDuplicatedException(ReviewDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.REVIEW_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundReviewException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundReviewException(NotFoundReviewException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_REVIEW;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(MenuDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleMenuDuplicatedException(MenuDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.MENU_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}



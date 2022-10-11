package com.udoolleh.backend.exception;


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
    @ExceptionHandler(CustomJwtRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleJwtException(CustomJwtRuntimeException e) {

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INVALID_JWT_TOKEN.getCode())
                .message(ErrorCode.INVALID_JWT_TOKEN.getMessage())
                .status(ErrorCode.INVALID_JWT_TOKEN.getStatus().value())
                .build();

        return new ResponseEntity<>(response, ErrorCode.INVALID_JWT_TOKEN.getStatus());
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

    @ExceptionHandler(NotFoundBoardException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundBoardException(NotFoundBoardException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_BOARD;

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

    @ExceptionHandler(NotFoundMenuException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundMenuException(NotFoundMenuException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_MENU;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(RestaurantDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleRestaurantDuplicatedException(RestaurantDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.RESTAURANT_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundPhotoException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundPhotoException(NotFoundPhotoException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_PHOTO;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundLikesException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundLikesException(NotFoundLikesException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_LIKES;

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

    @ExceptionHandler(LikesDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleLikesDuplicatedException(LikesDuplicatedException e){
        ErrorCode errorCode = ErrorCode.LIKES_DUPLICATED;
        
        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
   }

    @ExceptionHandler(UserNicknameDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleUserNicknameDuplicatedException(UserNicknameDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.USER_NICKNAME_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(TravelCourseDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleTravelCourseDuplicatedException(TravelCourseDuplicatedException e) {
        ErrorCode errorCode = ErrorCode.TRAVEL_COURSE_DUPLICATED;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
    @ExceptionHandler(NotFoundBoardCommentException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundBoardCommentException(NotFoundBoardCommentException e) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_BOARD_COMMENT;

        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}



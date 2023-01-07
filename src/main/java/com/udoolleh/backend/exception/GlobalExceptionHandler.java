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
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
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

    @ExceptionHandler(RegisterFailedException.class)
    protected ResponseEntity<ErrorResponse> handleRegisterFailedException(RegisterFailedException e) {
        return handleException(e, ErrorCode.REGISTER_FAILED);
    }

    @ExceptionHandler(LoginFailedException.class)
    protected ResponseEntity<ErrorResponse> handleLoginFailedException(LoginFailedException e) {
        return handleException(e, ErrorCode.LOGIN_FAILED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        //Content-Type 이 잘못된 경우
        return handleException(e, ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(CustomJwtRuntimeException.class)
    protected ResponseEntity<ErrorResponse> handleJwtException(CustomJwtRuntimeException e) {
        return handleException(e, ErrorCode.INVALID_JWT_TOKEN);
    }

    @ExceptionHandler(HarborNameDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleHarborNameDuplicatedException(HarborNameDuplicatedException e) {
        return handleException(e, ErrorCode.HARBOR_NAME_DUPLICATED);
    }

    @ExceptionHandler(NotFoundHarborException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundHarborException(NotFoundHarborException e) {
        return handleException(e, ErrorCode.NOT_FOUND_HARBOR);
    }

    @ExceptionHandler(HarborPeriodDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleHarborPeriodDuplicatedException(HarborPeriodDuplicatedException e) {
        return handleException(e, ErrorCode.HARBOR_PERIOD_DUPLICATED);
    }

    @ExceptionHandler(NotFoundHarborTimetableException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundHarborTimetableException(NotFoundHarborTimetableException e) {
        return handleException(e, ErrorCode.NOT_FOUND_HARBOR_TIMETABLE);
    }

    @ExceptionHandler(NotFoundUserException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundUserException(NotFoundUserException e) {
        return handleException(e, ErrorCode.NOT_FOUND_USER);
    }

    @ExceptionHandler(NotFoundPhotoException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundPhotoException(NotFoundPhotoException e) {
        return handleException(e, ErrorCode.NOT_FOUND_PHOTO);
    }

    @ExceptionHandler(NotFoundReviewException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundReviewException(NotFoundReviewException e) {
        return handleException(e, ErrorCode.NOT_FOUND_REVIEW);
    }

    @ExceptionHandler(ReviewDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleReviewDuplicatedException(ReviewDuplicatedException e) {
        return handleException(e, ErrorCode.REVIEW_DUPLICATED);
    }

    @ExceptionHandler(UserNicknameDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleUserNicknameDuplicatedException(UserNicknameDuplicatedException e) {
        return handleException(e, ErrorCode.USER_NICKNAME_DUPLICATED);
    }

    @ExceptionHandler(NotFoundTravelPlaceException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundTravelPlaceException(NotFoundTravelPlaceException e) {
        return handleException(e, ErrorCode.NOT_FOUND_TRAVEL_PLACE);
    }

    @ExceptionHandler(TravelPlaceDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleTravelPlaceDuplicatedException(TravelPlaceDuplicatedException e) {
        return handleException(e, ErrorCode.TRAVEL_PLACE_DUPLICATED);
    }

    @ExceptionHandler(ShipFareDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleShipFareDuplicatedException(ShipFareDuplicatedException e) {
        return handleException(e, ErrorCode.SHIP_FARE_DUPLICATED);
    }
    @ExceptionHandler(NotFoundShipFareException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundShipFareException(NotFoundShipFareException e) {
        return handleException(e, ErrorCode.NOT_FOUND_SHIP_FARE);
    }
    @ExceptionHandler(RegisterFileToS3FailedException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundShipFareException(RegisterFileToS3FailedException e) {
        return handleException(e, ErrorCode.REGISTER_FILE_TO_S3_FAILED);
    }
}



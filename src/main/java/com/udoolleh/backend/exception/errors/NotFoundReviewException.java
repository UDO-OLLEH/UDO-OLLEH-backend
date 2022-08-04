package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundReviewException extends RuntimeException{
    public NotFoundReviewException(){
        super(ErrorCode.NOT_FOUND_REVIEW.getMessage());
    }

    public NotFoundReviewException(Exception ex){
        super(ex);
    }
}

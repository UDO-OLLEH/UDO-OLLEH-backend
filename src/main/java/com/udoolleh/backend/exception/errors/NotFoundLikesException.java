package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundLikesException extends RuntimeException{
    public NotFoundLikesException() {
        super(ErrorCode.NOT_FOUND_LIKES.getMessage());
    }

    public NotFoundLikesException(Exception ex){
        super(ex);
    }
}

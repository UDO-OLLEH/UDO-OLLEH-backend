package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class ReviewDuplicatedException extends RuntimeException {

    public ReviewDuplicatedException(){
        super(ErrorCode.REVIEW_DUPLICATED.getMessage());
    }

    public ReviewDuplicatedException(Exception ex){
        super(ex);
    }
}
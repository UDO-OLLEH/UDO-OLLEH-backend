package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException(){
        super(ErrorCode.NOT_FOUND_USER.getMessage());
    }

    public NotFoundUserException(Exception ex){
        super(ex);
    }
}

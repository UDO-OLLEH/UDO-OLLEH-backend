package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundMenuException extends RuntimeException{
    public NotFoundMenuException(){
        super(ErrorCode.NOT_FOUND_MENU.getMessage());
    }

    public NotFoundMenuException(Exception ex){
        super(ex);
    }
}

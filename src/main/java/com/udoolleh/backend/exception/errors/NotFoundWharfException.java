package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundWharfException extends RuntimeException{
    public NotFoundWharfException(){
        super(ErrorCode.NOT_FOUND_WHARF.getMessage());
    }

    public NotFoundWharfException(Exception ex){
        super(ex);
    }
}

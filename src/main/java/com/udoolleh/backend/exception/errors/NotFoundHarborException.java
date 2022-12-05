package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundHarborException extends RuntimeException{
    public NotFoundHarborException(){
        super(ErrorCode.NOT_FOUND_HARBOR.getMessage());
    }

    public NotFoundHarborException(Exception ex){
        super(ex);
    }
}

package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class WharfTimeDuplicatedException extends RuntimeException{
    public WharfTimeDuplicatedException(){
        super(ErrorCode.WHARF_TIME_DUPLICATED.getMessage());
    }

    public WharfTimeDuplicatedException(Exception ex){
        super(ex);
    }
}

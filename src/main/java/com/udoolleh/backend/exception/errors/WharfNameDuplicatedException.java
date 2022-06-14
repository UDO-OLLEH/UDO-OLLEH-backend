package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class WharfNameDuplicatedException extends RuntimeException {

    public WharfNameDuplicatedException(){
        super(ErrorCode.WHARF_NAME_DUPLICATED.getMessage());
    }

    public WharfNameDuplicatedException(Exception ex){
        super(ex);
    }
}
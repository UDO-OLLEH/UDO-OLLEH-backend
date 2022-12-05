package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class HarborNameDuplicatedException extends RuntimeException {

    public HarborNameDuplicatedException(){
        super(ErrorCode.HARBOR_NAME_DUPLICATED.getMessage());
    }

    public HarborNameDuplicatedException(Exception ex){
        super(ex);
    }
}
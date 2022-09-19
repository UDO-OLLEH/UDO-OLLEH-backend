package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class UserDuplicatedException extends RuntimeException {

    public UserDuplicatedException(){
        super(ErrorCode.USER_DUPLICATED.getMessage());
    }

    public UserDuplicatedException(Exception ex){
        super(ex);
    }
}
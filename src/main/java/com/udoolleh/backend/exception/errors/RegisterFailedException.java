package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class RegisterFailedException extends RuntimeException{
    public RegisterFailedException(){
        super(ErrorCode.REGISTER_FAILED.getMessage());
    }
    public RegisterFailedException(Exception ex){
        super(ex);
    }
}

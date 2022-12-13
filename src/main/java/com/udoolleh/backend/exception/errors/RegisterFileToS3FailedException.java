package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class RegisterFileToS3FailedException extends RuntimeException {

    public RegisterFileToS3FailedException(){
        super(ErrorCode.USER_NICKNAME_DUPLICATED.getMessage());
    }

    public RegisterFileToS3FailedException(Exception ex){
        super(ex);
    }
}
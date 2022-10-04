package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class UserNicknameDuplicatedException extends RuntimeException {

    public UserNicknameDuplicatedException(){
        super(ErrorCode.USER_NICKNAME_DUPLICATED.getMessage());
    }

    public UserNicknameDuplicatedException(Exception ex){
        super(ex);
    }
}
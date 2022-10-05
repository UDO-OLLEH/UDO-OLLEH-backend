package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class LikesDuplicatedException extends RuntimeException{
    public LikesDuplicatedException(){
        super(ErrorCode.LIKES_DUPLICATED.getMessage());
    }
    public LikesDuplicatedException(Exception ex){
        super(ex);
    }
}

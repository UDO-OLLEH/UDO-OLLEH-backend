package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundBoardException extends RuntimeException{
    public NotFoundBoardException(){
        super(ErrorCode.NOT_FOUND_BOARD.getMessage());
    }

    public NotFoundBoardException(Exception ex){
        super(ex);
    }
}

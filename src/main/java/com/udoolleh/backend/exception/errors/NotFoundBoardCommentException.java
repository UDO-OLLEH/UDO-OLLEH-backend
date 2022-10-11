package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundBoardCommentException extends RuntimeException{
    public NotFoundBoardCommentException(){
        super(ErrorCode.NOT_FOUND_BOARD_COMMENT.getMessage());
    }

    public NotFoundBoardCommentException(Exception ex){
        super(ex);
    }
}
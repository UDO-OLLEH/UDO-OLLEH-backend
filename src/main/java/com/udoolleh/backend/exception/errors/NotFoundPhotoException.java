package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundPhotoException extends RuntimeException{
    public NotFoundPhotoException(){
        super(ErrorCode.NOT_FOUND_PHOTO.getMessage());
    }

    public NotFoundPhotoException(Exception ex){
        super(ex);
    }
}

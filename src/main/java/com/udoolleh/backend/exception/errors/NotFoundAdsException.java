package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundAdsException extends RuntimeException{
    public NotFoundAdsException(){
        super(ErrorCode.NOT_FOUND_ADS.getMessage());
    }

    public NotFoundAdsException(Exception ex){
        super(ex);
    }
}

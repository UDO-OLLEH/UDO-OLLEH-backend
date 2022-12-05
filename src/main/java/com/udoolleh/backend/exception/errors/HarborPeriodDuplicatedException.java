package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class HarborPeriodDuplicatedException extends RuntimeException{
    public HarborPeriodDuplicatedException(){
        super(ErrorCode.HARBOR_PERIOD_DUPLICATED.getMessage());
    }

    public HarborPeriodDuplicatedException(Exception ex){
        super(ex);
    }
}

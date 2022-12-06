package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class ShipFareDuplicatedException extends RuntimeException {

    public ShipFareDuplicatedException(){
        super(ErrorCode.SHIP_FARE_DUPLICATED.getMessage());
    }

    public ShipFareDuplicatedException(Exception ex){
        super(ex);
    }
}
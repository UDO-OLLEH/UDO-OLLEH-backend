package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class RestaurantDuplicatedException extends RuntimeException{

    public RestaurantDuplicatedException(){
        super(ErrorCode.RESTAURANT_DUPLICATED.getMessage());
    }

    public RestaurantDuplicatedException(Exception ex){
        super(ex);
    }

}

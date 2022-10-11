package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class TravelCourseDuplicatedException extends RuntimeException {

    public TravelCourseDuplicatedException(){
        super(ErrorCode.TRAVEL_COURSE_DUPLICATED.getMessage());
    }

    public TravelCourseDuplicatedException(Exception ex){
        super(ex);
    }
}
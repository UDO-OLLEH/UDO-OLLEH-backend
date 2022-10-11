package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundTravelCourseException extends RuntimeException{
    public NotFoundTravelCourseException(){
        super(ErrorCode.NOT_FOUND_TRAVEL_COURSE.getMessage());
    }

    public NotFoundTravelCourseException(Exception ex){
        super(ex);
    }
}

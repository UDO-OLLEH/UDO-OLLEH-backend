package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundTravelPlaceException extends RuntimeException {
    public NotFoundTravelPlaceException() {
        super(ErrorCode.NOT_FOUND_TRAVEL_PLACE.getMessage());
    }

    public NotFoundTravelPlaceException(Exception ex) {
        super(ex);
    }
}

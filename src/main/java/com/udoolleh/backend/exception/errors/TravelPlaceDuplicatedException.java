package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class TravelPlaceDuplicatedException extends RuntimeException {
    public TravelPlaceDuplicatedException() {
        super(ErrorCode.TRAVEL_PLACE_DUPLICATED.getMessage());
    }

    public TravelPlaceDuplicatedException(Exception ex) {
        super(ex);
    }
}

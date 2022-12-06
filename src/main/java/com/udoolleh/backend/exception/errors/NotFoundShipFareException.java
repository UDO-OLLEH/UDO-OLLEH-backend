package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundShipFareException extends RuntimeException {
    public NotFoundShipFareException() {
        super(ErrorCode.NOT_FOUND_SHIP_FARE.getMessage());
    }

    public NotFoundShipFareException(Exception ex) {
        super(ex);
    }
}
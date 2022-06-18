package com.udoolleh.backend.exception.errors;

import com.udoolleh.backend.exception.ErrorCode;

public class NotFoundWharfTimetableException extends RuntimeException {
    public NotFoundWharfTimetableException() {
        super(ErrorCode.NOT_FOUND_WHARF_TIMETABLE.getMessage());
    }

    public NotFoundWharfTimetableException(Exception ex) {
        super(ex);
    }
}
